package br.com.ada.programacaowebii.aula.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class RickAndMortyService {

    /**
     * Retorna um apelido buscado da API Rick and Morty de forma <strong>determinística</strong> utilizando o hash do nome.
     * Apelidos sempre serão os mesmos dado que for passada a mesma String.
     * @param nome
     * @return String apelido
     */
    public String getApelido(String nome){
        Integer count  = getQtdPersonagensGraphql();
        Integer numPersonagem = nameToNumber(nome, count);
        return getNomePersonagemGraphQl(numPersonagem);
    }

    /**
     * Busca nome de um Personagem na API por id.
     * @param id id do personagem.
     * @return Nome de um personagem.
     */
    private String getNomePersonagem(Integer id){

        WebClient webClient = WebClient.create();
        String uri = String.format("https://rickandmortyapi.com/api/character/%s", id);
        WebClient.ResponseSpec retrieve = webClient.get().uri(uri).retrieve();
        String responseBody = retrieve.bodyToMono(String.class).block();

        JSONObject obj = new JSONObject(responseBody);
        return obj.getString("name");
    }

    /**
     * Busca nome de um personagem utilizando uma Query Graphql, evitando o overfetching de informações.
     * @param id
     * @return Nome de um personagem.
     */
    private String getNomePersonagemGraphQl(Integer id){

        WebClient webClient = WebClient.create();
        String uri = "https://rickandmortyapi.com/graphql";
        String query = String.format("{ \"query\": \"{ character(id: %d) { name } }\" }", id) ;
        WebClient.ResponseSpec retrieve = webClient
                .post()
                .uri(uri)
                .header("content-type","application/json")
                .bodyValue(query)
                .retrieve();
        String responseBody = retrieve.bodyToMono(String.class).block();

        JSONObject obj = new JSONObject(responseBody);
        obj = obj.getJSONObject("data").getJSONObject("character");
        return obj.getString("name");
    }

    /**
     * Busca a quantidade de personagens disponíveis na API até o momento.
     * @return Quantidade de personagens na API.
     */
    private Integer getQtdPersonagens(){
        WebClient webClient = WebClient.create();
        String uri = "https://rickandmortyapi.com/api/character";
        WebClient.ResponseSpec retrieve = webClient.get().uri(uri).retrieve();
        String responseBody = retrieve.bodyToMono(String.class).block();


        JSONObject obj = new JSONObject(responseBody);
        JSONObject info =  obj.getJSONObject("info");
        Integer count = info.getInt("count");
        return count;
    }

    /**
     * Busca a quantidade de personagens disponíveis na API até o momento.
     * @return Quantidade de personagens na API.
     */
    private Integer getQtdPersonagensGraphql(){
        WebClient webClient = WebClient.create();
        String uri = "https://rickandmortyapi.com/graphql";
        String query = "{ \"query\": \"{ characters{ info{ count } } }\" }";

        WebClient.ResponseSpec retrieve = webClient
                .post()
                .uri(uri)
                .header("content-type","application/json")
                .bodyValue(query)
                .retrieve();
        String responseBody = retrieve.bodyToMono(String.class).block();

        return new JSONObject(responseBody)
                .getJSONObject("data")
                .getJSONObject("characters")
                .getJSONObject("info")
                .getInt("count");
    }

    /**
     * Transforma uma String em um número de 1 à qtdMaxima
     * @param name Nome a ser transformado
     * @param qtdMaxima Quantidade de personagens na
     */
    private static Integer nameToNumber(String name, Integer qtdMaxima) {

        BigInteger qtdBigInteger = BigInteger.valueOf(qtdMaxima);

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(name.getBytes(), 0, name.length());
        BigInteger nameHash = new BigInteger(1, md5.digest());

        //Soma 1 pois não existe personagem com id 0
        return nameHash.mod(qtdBigInteger).add(BigInteger.ONE).intValue();
    }
}
