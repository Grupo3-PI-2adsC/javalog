package org.example.pc.componentes;
import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;
import org.springframework.jdbc.core.JdbcTemplate;

public class RamCp extends Componente {

//    private Memoria memoria;

    public RamCp(Integer fkMaquina) {
        super(fkMaquina);
//        this.memoria = new Memoria();
    }
//
//    public Memoria getMemoria() {
//        return memoria;
//    }

    @Override
    public void buscarInfosFixos(Boolean integer) {

        ConexaoMysql con = new ConexaoMysql();
        Looca looca = new Looca();


        Long totalMemoria = (looca.getMemoria().getTotal() / 1000000000);

        String queryMemoria = """
                    INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                            ( %d, 2, 'total de memoria do computador', '%s', 'total de memoria do computador')
                """.formatted(
                fkMaquina,
                totalMemoria
        );

        con.executarQuery(queryMemoria);
        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(queryMemoria);
        }

    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {

        Looca looca = new Looca();
        Long emUsoMemoria = (looca.getMemoria().getEmUso() / 1000000000);

        if (teste) {
            ConexaoSqlserver conexao = new ConexaoSqlserver();
            JdbcTemplate con = conexao.getConexaoDoBanco();

            try {
                String queryFk = """
                        select idDadosFixos from dadosFixos where fkMaquina = %d and fkTipoComponente = 2 and nomeCampo = 'total de memoria do computador'""".formatted(fkMaquina);
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
                         ,valorCampo) VALUES
                           ( %d, %d, 2, current_timestamp,'emUso', '%s');
                            """.formatted(
                        fk,
                        fkMaquina,
                        emUsoMemoria
                );

                conexao.executarQuery(queryMemoria);

            } catch (Exception erro) {
                System.out.println(erro);
            }
        }
        ConexaoMysql conexao1 = new ConexaoMysql();
        JdbcTemplate con1 = conexao1.getConexaoDoBanco();

        try{
            String queryFk = """
                    select idDadosFixos from dadosFixos where fkMaquina = %d and fkTipoComponente = 2 and nomeCampo = 'total de memoria do computador'""".formatted(fkMaquina);
            System.out.println(queryFk);
            Integer fk = con1.queryForObject(queryFk,Integer.class);
            System.out.println(fk);

            var queryMemoria= """
                    
                        iNSERT INTO dadosTempoReal(
                         fkDadosFixos
                         ,fkMaquina
                         ,fkTipoComponente
                         ,dataHora
                         ,nomeCampo
                         ,valorCampo)VALUES
                         ( %d, %d, 2, current_timestamp(),'emUso', '%s');
                           """.formatted(
                    fk,
                    fkMaquina,
                    emUsoMemoria
            );



            conexao1.executarQuery(queryMemoria);
        }catch (Exception erro){
            System.out.println(erro);
        }


    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca = new Looca();
        ConexaoMysql con1 = new ConexaoMysql();

        Long totalMemoria = (looca.getMemoria().getTotal() / 1000000000);


        String sql21 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'total de memoria do computador';
                """.formatted(
                totalMemoria,
                fkMaquina,
                2);

        con1.executarQuery(sql21);

        if (servidor){
            ConexaoSqlserver con = new ConexaoSqlserver();
            con.executarQuery(sql21);
        }

    }
}
