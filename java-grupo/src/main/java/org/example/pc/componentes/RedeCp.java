package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RedeCp extends Componente {

    public RedeCp(Integer fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {


        ConexaoMysql con1 = new ConexaoMysql();

        Looca looca = new Looca();

        String nomeRede = looca.getRede().getParametros().getNomeDeDominio();
        String nomeExibicaoRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getNomeExibicao();
        List enderecoIPv4Rede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv4();
        List enderecoIPv6Rede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv6();
        String enderecoMACRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoMac();
        String hostnameRede = looca.getRede().getParametros().getHostName();
        List servidoresDNSRede = looca.getRede().getParametros().getServidoresDns();

        String queryRede = """
                    INSERT INTO fixosRede 
                   (fkMaquina
                   ,nomeCampo
                   ,valorCampo)
                   VALUES
                                            ( %d, 'Nome da rede', '%s'),
                                            ( %d, 'Nome de exibição da rede', '%s'),
                                            ( %d, 'Endereço IPv4 da rede', '%s'),
                                            ( %d, 'Endereço IPv6 da rede', '%s'),
                                            ( %d, 'Endereço MAC da rede', '%s'),
                                            ( %d, 'hostname da rede', '%s'),
                                            ( %d, 'servidores DNS da rede', '%s')
                """.formatted(
                fkMaquina,
                nomeRede,
                fkMaquina,
                nomeExibicaoRede,
                fkMaquina,
                enderecoIPv4Rede,
                fkMaquina,
                enderecoIPv6Rede,
                fkMaquina,
                enderecoMACRede,
                fkMaquina,
                hostnameRede,
                fkMaquina,
                servidoresDNSRede
        );

        con1.executarQuery(queryRede);
        if (integer == true){
            ConexaoSqlserver con = new ConexaoSqlserver();
            con.executarQuery(queryRede);
        }
    }


    @Override
    public void buscarInfosVariaveis(Boolean teste) {

//        System.out.println("aisdpasindpas");
        Looca looca = new Looca();
        Long pacotesRecebidosRede = (looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getPacotesRecebidos());
        Long pacotesEnviadosRede = (looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getPacotesEnviados());
            String queryFk = """
                        select idFixosRede from fixosRede where fkMaquina = %d and nomeCampo = '%s'""".formatted(fkMaquina, "hostname da rede");

        if (teste) {
            ConexaoSqlserver conexao = new ConexaoSqlserver();
            JdbcTemplate con = conexao.getConexaoDoBanco();


            String host = looca.getRede().getParametros().getHostName();
            try {

                System.out.println(queryFk);
                Integer fk = con.queryForObject(queryFk, Integer.class);
                System.out.println(fk);


                var queryRede = """
                        iNSERT INTO variaveisRede
                        (fkFixosRede
                        ,fkMaquina
                        ,dataHora
                        ,nomeCampo
                        ,valorCampo)VALUES
                        ( %d, 4, current_timestamp,'Pacotes recebidos', '%s'),
                        ( %d, 4, current_timestamp,'Pacotes enviados', '%s');
                               """.formatted(
                        fk,
                        pacotesRecebidosRede,
                        fkMaquina,
                        pacotesEnviadosRede
                );
                conexao.executarQuery(queryRede);
            } catch (Exception erro) {
                System.out.println(erro);
            }

        }


        ConexaoMysql conexao1 = new ConexaoMysql();
        JdbcTemplate con1 = conexao1.getConexaoDoBanco();
        try {

            System.out.println(queryFk);
            Integer fk = con1.queryForObject(queryFk, Integer.class);
            System.out.println(fk);


            var queryRede1 = """
                    iNSERT INTO variaveisRede 
                    (fkFixosRede
                    ,fkMaquina
                    ,dataHora
                    ,nomeCampo
                    ,valorCampo)VALUES
                                                       ( %d, %d, current_timestamp(),'Pacotes recebidos', '%s'),
                                                       ( %d, %d, current_timestamp(),'Pacotes enviados', '%s');
                           """.formatted(
                    fkMaquina,
                    fk,
                    pacotesRecebidosRede,
                    fkMaquina,
                    fk,
                    pacotesEnviadosRede
            );
            conexao1.executarQuery(queryRede1);
        }catch (Exception erro){
            System.out.println(erro);
        }


    }


    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca = new Looca();
        ConexaoMysql con1 =  new ConexaoMysql();

        String nomeRede = looca.getRede().getParametros().getNomeDeDominio();
        String nomeExibicaoRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getNomeExibicao();
        List enderecoIPv4Rede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv4();
        List enderecoIPv6Rede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoIpv6();
        String enderecoMACRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoMac();
        String hostnameRede = looca.getRede().getParametros().getHostName();
        List servidoresDNSRede = looca.getRede().getParametros().getServidoresDns();



        String sql9 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'servidores DNS da rede';
                """.formatted(
                servidoresDNSRede,
                fkMaquina,
                4);

        con1.executarQuery(sql9);

        String sql10 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'hostname da rede';
                """.formatted(
                hostnameRede,
                fkMaquina,
                4);

        con1.executarQuery(sql10);

        String sql11 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Endereço MAC da rede';
                """.formatted(
                enderecoMACRede,
                fkMaquina,
                4);

        con1.executarQuery(sql11);

        String sql12 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Endereço IPv6 da rede';
                """.formatted(
                enderecoIPv6Rede,
                fkMaquina,
                4);

        con1.executarQuery(sql12);

        String sql13 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Endereço IPv4 da rede';
                """.formatted(
                enderecoIPv4Rede,
                fkMaquina,
                4);

        con1.executarQuery(sql13);

        String sql14 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'enderecoIPv4';
                """.formatted(
                nomeExibicaoRede,
                fkMaquina,
                4);

        con1.executarQuery(sql14);

        String sql15 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Nome de exibição da rede';
                """.formatted(
                nomeExibicaoRede,
                fkMaquina,
                4);

        con1.executarQuery(sql15);

        String sql16 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'Nome da rede';
                """.formatted(
                nomeRede,
                fkMaquina,
                4);

        con1.executarQuery(sql16);

        if (servidor){
            ConexaoSqlserver con =  new ConexaoSqlserver();
            con.executarQuery(sql16);
            con.executarQuery(sql15);
            con.executarQuery(sql9);
            con.executarQuery(sql10);
            con.executarQuery(sql11);
            con.executarQuery(sql14);

        }

    }


}
