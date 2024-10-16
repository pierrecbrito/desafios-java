package br.com.pierrecbrito.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeraEmailsInstitucionais {
    static final String URL_FONTE = "https://dados.ufrn.br/api/action/datastore_search?resource_id=6a8e5461-e748-45c6-aac6-432188d88dde&limit=5";

    public static void carregarDados() {
        try {
            URL obj = new URL(URL_FONTE);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Configura o método de requisição
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Imprime a resposta
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        carregarDados();
    }
}
