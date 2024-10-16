package br.com.pierrecbrito.java;

public class Abreviacoes {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, forneça um nome completo como argumento.");
            return;
        }

        String nomeCompleto = String.join(" ", args);
        String[] palavras = nomeCompleto.split(" ");
        StringBuilder abreviacao = new StringBuilder();

        for (String palavra : palavras) {
            if (palavra.length() > 2) {
                abreviacao.append(palavra.charAt(0)).append(". ");
            } else {
                abreviacao.append(palavra).append(" ");
            }
        }

        System.out.println(abreviacao.toString().trim());
    }
}
