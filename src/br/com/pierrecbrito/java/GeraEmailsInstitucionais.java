package br.com.pierrecbrito.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GeraEmailsInstitucionais {
    static final String URL_FONTE = "https://dados.ufrn.br/api/action/datastore_search?resource_id=6a8e5461-e748-45c6-aac6-432188d88dde";
    private static final Set<String> CONECTORES = new HashSet<>(Arrays.asList(
            "de", "da", "das", "do", "dos"
    ));

    /***
     * Carrega os dados dos docentes da UFRN
     * @return - JSONArray com os dados dos docentes
     */
    public static JSONArray carregarDados() {
        try {
            JSONArray records = new JSONArray();

            // Faz a requisição GET para a API de dados abertos da UFRN
            URL obj = new URL(URL_FONTE);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Analisa a resposta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            int total = jsonResponse.getJSONObject("result").getInt("total");
            //Retorna todas as páginas de com os dados de docentes
            while(records.length() != total) {
                records.putAll(jsonResponse.getJSONObject("result").getJSONArray("records"));
                obj = new URL(URL_FONTE + "&offset=" + records.length());
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                jsonResponse = new JSONObject(response.toString());
            }
            records.putAll(jsonResponse.getJSONObject("result").getJSONArray("records"));

            return records;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isPreposicao(String palavra) {
        return CONECTORES.contains(palavra.toLowerCase());
    }

    /***
     * Gera o email institucional de um docente com seu nome inicial e o último nome
     * @param nomeCompleto - Nome completo do docente
     * @return - Email institucional do docente simples+
     */
    public static String gerarEmailSimples(String nomeCompleto) {
        String primeiroNome = nomeCompleto.split(" ")[0];
        String sobrenome = nomeCompleto.split(" ")[nomeCompleto.split(" ").length - 1];

        String email = primeiroNome.toLowerCase() + "." +  sobrenome.toLowerCase() + "@ufrn.edu.br";
        return email;
    }

    /***
     * Gera o email institucional de um docente com todos os seus nomes
     * @param nomeCompleto - Nome completo do docente
     * @return - Email institucional do docente complexo
     */
    public static String gerarEmailComplexo(String nomeCompleto) {
        String[] nomeSeparado = nomeCompleto.split(" ");
        StringBuilder email = new StringBuilder();

        for(int i = 0; i < nomeSeparado.length; i++) {
            if(!isPreposicao(nomeSeparado[i]) && i != nomeSeparado.length - 1) {
               email.append(nomeSeparado[i].toLowerCase() + ".");
            } else if(i == nomeSeparado.length - 1) {
                email.append(nomeSeparado[i].toLowerCase());
            }
        }

        email = email.append("@ufrn.edu.br");
        return email.toString();
    }

    /***
     * Gera 20 índices aleatórios para acessar os dados dos docentes
     * @param tamanhoColecao - Tamanho da coleção de docentes
     * @return - Conjunto com 20 índices aleatórios
     */
    public static Set<Integer> gerarIndices(int tamanhoColecao) {
        Set<Integer> indices = new HashSet<>();
        Random random = new Random();

        while (indices.size() < 20) {
            int indice = random.nextInt(tamanhoColecao);
            indices.add(indice);
        }

        return indices;
    }

    public static void main(String[] args) {
        JSONArray docentes = carregarDados();
        Set<Integer> indices = gerarIndices(docentes.length());
        List<String> emails = new ArrayList<>();

        for (Integer indice : indices) {
            JSONObject docente = docentes.getJSONObject(indice);
            String nome = docente.getString("nome");
            String email = gerarEmailSimples(nome);
            if(emails.contains(email)) {
                email = gerarEmailComplexo(nome);
            }
            emails.add(email);
            System.out.println(nome + " - " + email);
        }

        System.out.println("PIERRE CARLOS DE BRITO - " + gerarEmailComplexo("PIERRE CARLOS DE BRITO"));
    }
}
