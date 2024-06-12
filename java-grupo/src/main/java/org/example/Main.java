package org.example;

import org.example.pc.Computador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static boolean validou;

    public static void main(String[] args) {

//        Conexao conexao = new Conexao(1);
//        JdbcTemplate con = conexao.getConexaoDoBanco();
//
//        con.execute("     INSERT INTO usuario VALUES\n" +
//                "        ( 'recepção', 'KET', 'ket@netmet.com', '1234', 1, 1);");

//    Computador com = new Computador();
//
//    com.teste();
//        Looca looca = new Looca();
//        List<Volume> teste = looca.getGrupoDeDiscos().getVolumes();
//        Integer qtdVolumesVolume = looca.getGrupoDeDiscos().getQuantidadeDeVolumes();
//        System.out.println(qtdVolumesVolume - 1);
//
//        for (int i = 1; i < qtdVolumesVolume; i++) {
//
//            //            VOLUME
//            String UUIDVolume = teste.get(i).getUUID();
//            String nomeVolume = teste.get(i).getNome();
//            Long totalVolume = teste.get(i).getTotal();
//            Long disponivelVolume = teste.get(i).getDisponivel();
//            String tipoVolume = teste.get(i).getTipo();
//
//            System.out.println(nomeVolume);
//        }
//


//        Conexao conexao = new Conexao();
//        JdbcTemplate con = conexao.getConexaoDoBanco();


//        Computador sla = new Computador(1, "matteus-Nitro-AN515-57", true, 1);
//
//        System.out.println(sla);

//        idMaquina=1, hostname='matteus-Nitro-AN515-57', ativo=true, empresa=1, listaComponentes=[org.example.pc.componentes.RamCp@11981797, org.example.pc.componentes.DiscoCp@774698ab, org.example.pc.componentes.ProcessadorCp@3543df7d, org.example.pc.componentes.RedeCp@17a87e37, org.example.pc.componentes.SistemaCp@20f12539, org.example.pc.componentes.VolumeCp@57459491], rede=org.example.pc.componentes.RedeCp@17a87e37, disco=org.example.pc.componentes.DiscoCp@774698ab, processador=org.example.pc.componentes.ProcessadorCp@3543df7d, ram=org.example.pc.componentes.RamCp@11981797, volume=org.example.pc.componentes.VolumeCp@57459491, sistema=org.example.pc.componentes.SistemaCp@20f12539}




        Scanner input = new Scanner(System.in);
        Scanner inputText = new Scanner(System.in);

        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |           Digite o seu email:        |
                    |                                      |
                    ----------------------------------------""");

        String emailLogin = inputText.nextLine();


        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |           Digite a sua senha:        |
                    |                                      |
                    ----------------------------------------""");
        String senhaLogin = inputText.nextLine();

        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |        Confirmar a sua senha:        |
                    |                                      |
                    ----------------------------------------""");
        String confirmarSenhaLogin = inputText.nextLine();

        registrarTentativaLogin(emailLogin, validou);

        ConexaoSqlserver con = new ConexaoSqlserver();
//        Computador computador = new Computador();
        try {
            con.buscarCredenciais(emailLogin, senhaLogin);
            Computador computador = con.computadorExiste(1, true);

            try {
                ConexaoMysql conexaoMysql = new ConexaoMysql();
                conexaoMysql.buscarCredenciais(emailLogin, senhaLogin);

                System.out.println("""
                                Usuario Logado com sucesso
                                ..............................""");


                Computador computador1 = conexaoMysql.computadorExiste(1, true);
                System.out.println(computador1);
                computador.buscarInfos(1, true);

            } catch (Exception erro) {
                System.out.println(erro);
            }

        } catch (Exception erro) {
            System.out.println(erro);
            System.out.println("""
                                Erro ao fazer Login no SQL SERVER, tente novamente
                                .......................................""");
            if (erro.getCause() != null) {

                ConexaoMysql conexaoMysql = new ConexaoMysql();
                try {
                    conexaoMysql.buscarCredenciais(emailLogin, senhaLogin);

                    System.out.println("""
                                        Usuario Logado com sucesso localmente
                                        ..............................""");


                    Computador computador1 = conexaoMysql.computadorExiste(1, false);
                    System.out.println("main + " + computador1);

                } catch (Exception erro2) {
                    System.out.println("erro no main");
                    System.out.println(erro2);
                }
            }
        }

    }

    private static void registrarTentativaLogin(String email, boolean sucesso) {
        String logMessage = "Data e Hora: " + new Date() + "\n" +
                "Email: " + email + "\n" +
                "Resultado: " + (sucesso ? "Sucesso" : "Falha") + "\n";

        // Escrever no arquivo
        try (PrintWriter escritor = new PrintWriter(new FileWriter("log_login.txt", true))) {
            escritor.println(logMessage);
            escritor.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Imprimir no console
        System.out.println(logMessage);
    }
}
