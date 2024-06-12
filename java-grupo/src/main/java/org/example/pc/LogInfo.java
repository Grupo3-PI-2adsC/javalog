package org.example.pc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;
import java.util.Scanner;

public class LogInfo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Coletar dados");
            System.out.println("2. Encerrar");
            String escolha = scanner.nextLine();

            if (escolha.equals("1")) {
                String resultado = iniciarLog();
                System.out.println(resultado);
            } else if (escolha.equals("2")) {
                System.out.println("Até breve!");
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private static String iniciarLog() {
        StringBuilder resultado = new StringBuilder();
        try (PrintWriter escritor = new PrintWriter(new FileWriter("log_info.txt", true))) {
            resultado.append(logInfoSistema(escritor));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado.toString();
    }

    private static String logInfoSistema(PrintWriter escritor) {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        StringBuilder info = new StringBuilder();

        String dataHora = "Data e Hora: " + new Date();
        String sistemaOperacional = "Sistema Operacional: " + osBean.getName();
        String versaoSO = "Versão do SO: " + osBean.getVersion();
        String numProcessadores = "Número de Processadores: " + osBean.getAvailableProcessors();

        info.append(dataHora).append("\n")
                .append(sistemaOperacional).append("\n")
                .append(versaoSO).append("\n")
                .append(numProcessadores).append("\n");

        escritor.println(dataHora);
        escritor.println(sistemaOperacional);
        escritor.println(versaoSO);
        escritor.println(numProcessadores);
        escritor.println();
        escritor.flush();

        return info.toString();
    }
}
