package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.pc.Computador;
import org.example.ConexaoMysql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ConexaoMysql extends Conexao {


    private JdbcTemplate conexaoDoBanco;
    private String url;
    private String username;
    private String password;
    private Usuario userAtual = null;



    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Usuario getUserAtual() {
        return userAtual;
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }


    public ConexaoMysql() {
        BasicDataSource dataSource = new BasicDataSource();

        this.url = "jdbc:mysql://localhost:3306/netmed";
        this.username = "Netmed";
        this.password = "Netmed#1@@";


        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        /*
             Exemplo de string de conexões:
                jdbc:mysql://localhost:3306/mydb <- EXEMPLO PARA MYSQL
                jdbc:sqlserver://localhost:1433;database=mydb <- EXEMPLO PARA SQL SERVER
        */
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        this.conexaoDoBanco = new JdbcTemplate(dataSource);
    }

    @Override
    public Usuario buscarCredenciais(String email, String senha) {
        System.out.println("""
                |
                |
              MYSQL
                |
                |""");
        ConexaoMysql conexao = new ConexaoMysql();
        JdbcTemplate con = conexao.getConexaoDoBanco();

//        RowMapper<Usuario> userTesteRowMapper = (rs, rowNum) -> new Usuario(rs.getInt("id"),
//                rs.getString("email"), rs.getString("senha"));)
        RowMapper<Usuario> usuarioRowMapper = (resultSet, i) ->new Usuario(
                resultSet.getInt("idUsuario"), resultSet.getString("tipoUsuario"), resultSet.getString("nome"),
                resultSet.getString("email"), resultSet.getString("senha"), resultSet.getInt("fkEmpresa"));

        String sql = "select * from usuario where email = '%s' and senha = '%s';".formatted(email,senha);

        System.out.println("""
                    Executando a query:
                    %s
                    ...........................................""".formatted(sql));

        userAtual = con.queryForObject(sql,
                usuarioRowMapper);


        return userAtual;
    }

    @Override
    public Computador computadorExiste(Integer vez, Boolean servidor) {
        System.out.println("""
                |
                |
              MYSQL
                |
                |""");

        System.out.println("""
                            Verificando se o computador já existe na nossa base de dados
                            .............................................................""");

        ConexaoMysql conexao = new ConexaoMysql();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        Looca teste = new Looca();
        String hostname = teste.getRede().getParametros().getHostName();
        Integer arquitetura = teste.getSistema().getArquitetura();
        RowMapper<Computador> computadorRowMapper = (resultSet, i) ->new Computador(resultSet.getInt("idMaquina"), resultSet.getString("hostname"),
                resultSet.getBoolean("ativo"), resultSet.getInt("fkEmpresa"));

        String sql = "select idMaquina, hostname, ativo, fkEmpresa from maquina where hostName = '%s';".formatted(hostname);

            Computador computadorMonitorado;
        try {

            System.out.println("""
                    Executando a query:
                    %s
                    ...........................................""".formatted(sql));

            computadorMonitorado = con.queryForObject(sql,
                    computadorRowMapper);

//        System.out.println(computadorMonitorado + " asdasd");
            System.out.println("""
                    A Maquina em execução existe na base de dados
                    ...............................................""");

            String sqlAtivo = "update maquina set ativo = 1 where idMaquina = %d;".formatted(computadorMonitorado.getIdMaquina());
            conexao.executarQuery(sqlAtivo);
//            Computador já tem cadastrado os componentes
            System.out.println("vez:" + vez);
            if (vez == 1){

                computadorMonitorado.atualizarFixos(servidor);

                return computadorMonitorado;
            }else {

                System.out.println("""
                Cadastrando os Componentes fixos do computador
                ............................................""");
//                o computador acabou de ser cadastrado e ainda não possui componente

                computadorMonitorado.cadastrarFixos(servidor);
                System.out.println("|Dados fixos cadastrados|");
                System.out.println(computadorMonitorado);
                computadorMonitorado.buscarInfos(1, servidor);
            }

        }catch (Exception erro) {
            System.out.println(erro);
            if (erro.getCause() == null) {

                System.out.println("""
                                    Computador não existe. Vamos cadastralo agora
                                    ..............................................""");

                String sqlMaquina = "INSERT INTO maquina VALUES (null, '%s', %b, %d, %b, %d);".formatted(
                        hostname,
                        true,
                        arquitetura,
                        false,
                        userAtual.getFkEmpresa()
                );

                try {
                    con.execute(sqlMaquina);
                    System.out.println("""
                                        Computador Cadastrado com sucesso
                                        ...................................""");
                    computadorMonitorado = null;
                    conexao.computadorExiste(0, servidor);
                } catch (Exception erro2) {
                    System.out.println("""
                                          Erro: %s
                                          Causa do erro: %s
                                         .....................""".formatted(erro2, erro2.getCause()));
                }
            }
            System.out.println("Aqui???");
        }
            return null;
    }

    @Override
    public void executarQuery(String query) {
        System.out.println("""
                |
                |
              MYSQL
                |
                |""");
        System.out.println("Exec");

        System.out.println("""
                            Executando query:
                            %s
                            ........................""".formatted(query));

        ConexaoMysql conexao = new ConexaoMysql();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        con.execute(query);
    }
}
