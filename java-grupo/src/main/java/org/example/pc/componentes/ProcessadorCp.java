package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProcessadorCp extends Componente {
    public ProcessadorCp(Integer fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {


        ConexaoMysql con = new ConexaoMysql();

        Looca looca = new Looca();


        String nomeProcessador = looca.getProcessador().getNome();
        String potenciaProcessador = nomeProcessador.substring(nomeProcessador.indexOf("@") + 2, nomeProcessador.lastIndexOf("G"));
        Integer nmrPacotesFisicosProcessador = looca.getProcessador().getNumeroPacotesFisicos();
        Integer nmrCpusFisicosProcessador = looca.getProcessador().getNumeroCpusFisicas();
        Integer nmrCpusLogicasProcessador = looca.getProcessador().getNumeroCpusLogicas();

        String queryProcessador = """
                    INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                            ( %d, 3, 'Nome do processador', '%s', 'Nome do processador'),
                                            ( %d, 3, 'Numero de pacotes físicos do processador', '%s', 'Numero de pacotes físicos do processador'),
                                            ( %d, 3, 'Potencia do processador', '%s', 'Potencia do processador'),
                                            ( %d, 3, 'Numero de CPUs físicas do processador', '%s', 'Numero de CPUs físicas do processador'),
                                            ( %d, 3, 'Numero de CPUs Logicas do processador', '%s', 'Numero de CPUs Logicas do processador')
                """.formatted(
                fkMaquina,
                nomeProcessador,
                fkMaquina,
                nmrPacotesFisicosProcessador,
                fkMaquina,
                potenciaProcessador,
                fkMaquina,
                nmrCpusFisicosProcessador,
                fkMaquina,
                nmrCpusLogicasProcessador
        );

        con.executarQuery(queryProcessador);
        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(queryProcessador);
        }
    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {


        Looca looca = new Looca();
        Double emUsoProcessador = (looca.getProcessador().getUso());

        String queryFk = """
                select idDadosFixos from dadosFixos where fkMaquina = %d and fkTipoComponente = 3 and nomeCampo = 'Nome do processador'""".formatted(fkMaquina);


        if (teste) {
            ConexaoSqlserver conexao = new ConexaoSqlserver();
            JdbcTemplate con = conexao.getConexaoDoBanco();


            try {
                System.out.println(queryFk);
                Integer fk = con.queryForObject(queryFk, Integer.class);
                System.out.println(fk);

                var queryMemoria = """
                        iNSERT INTO dadosTempoReal(
                         fkDadosFixos
                         ,fkMaquina
                         ,fkTipoComponente
                         ,dataHora
                         ,nomeCampo
                         ,valorCampo) values
                        ( %d, %d, 3, current_timestamp,'emUso', '%s');
                              """.formatted(
                        fk,
                        fkMaquina,
                        emUsoProcessador
                );


                conexao.executarQuery(queryMemoria);
            } catch (Exception erro) {
                System.out.println(erro);
            }

        }

        ConexaoMysql conexao1 = new ConexaoMysql();
        JdbcTemplate con1 = conexao1.getConexaoDoBanco();

        try {


            Integer fk1 = con1.queryForObject(queryFk,Integer.class);

            var queryMemoria1= """
                     iNSERT INTO dadosTempoReal(
                      fkDadosFixos
                      ,fkMaquina
                      ,fkTipoComponente
                      ,dataHora
                      ,nomeCampo
                      ,valorCampo) values
                      ( %d, %d, 3, current_timestamp(),'emUso', '%s');
                           """.formatted(
                    fk1,
                    fkMaquina,
                    emUsoProcessador
            );

            conexao1.executarQuery(queryMemoria1);
        }catch (Exception erro){
            System.out.println(erro);
        }
    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca =  new Looca();
        ConexaoMysql con1 =  new ConexaoMysql();

        String nomeProcessador = looca.getProcessador().getNome();
        Integer nmrPacotesFisicosProcessador = looca.getProcessador().getNumeroPacotesFisicos();
        String potenciaProcessador = nomeProcessador.substring(nomeProcessador.indexOf("@") + 2, nomeProcessador.lastIndexOf("G"));
        Integer nmrCpusFisicosProcessador = looca.getProcessador().getNumeroCpusFisicas();
        Integer nmrCpusLogicasProcessador = looca.getProcessador().getNumeroCpusLogicas();



        String sql17 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Numero de CPUs Logicas do processador';
                """.formatted(
                nmrCpusLogicasProcessador,
                fkMaquina,
                3);

        con1.executarQuery(sql17);

        String sql18 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Numero de CPUs físicas do processador';
                """.formatted(
                nmrCpusFisicosProcessador,
                fkMaquina,
                3);

        con1.executarQuery(sql18);

        String sql = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Potencia do processador';
                """.formatted(
                potenciaProcessador,
                fkMaquina,
                3);

        con1.executarQuery(sql);

        String sql19 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Numero de pacotes físicos do processador';
                """.formatted(
                nmrPacotesFisicosProcessador,
                fkMaquina,
                3);

        con1.executarQuery(sql19);


        String sql20 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Nome do Processador';
                """.formatted(
                nomeProcessador,
                fkMaquina,
                3);

        con1.executarQuery(sql20);

        if (servidor){
            ConexaoSqlserver con =  new ConexaoSqlserver();
            con.executarQuery(sql17);
            con.executarQuery(sql18);
            con.executarQuery(sql);
            con.executarQuery(sql19);
            con.executarQuery(sql20);

        }

    }
}
