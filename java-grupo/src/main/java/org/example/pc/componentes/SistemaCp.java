package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;

import java.time.Instant;

public class SistemaCp extends Componente {
    public SistemaCp(Integer fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {


        ConexaoMysql con = new ConexaoMysql();


        Looca looca = new Looca();

        //            SISTEMA
        String modeloSistema = looca.getRede().getParametros().getHostName();
        Instant inicializadoSistema = looca.getSistema().getInicializado();

        String querySistema = """
                    INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                            ( %d, 1, 'modelo do Sistema', '%s', 'modelo do Sistema'),
                                            ( %d, 1, 'inicialização do sistema', '%s', 'inicialização do sistema');
                """.formatted(
                fkMaquina,
                modeloSistema,
                fkMaquina,
                inicializadoSistema
        );

        con.executarQuery(querySistema);
        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(querySistema);
        }
    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {

    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca = new Looca();
        ConexaoMysql con = new ConexaoMysql();

        String modeloSistema = looca.getRede().getParametros().getHostName();
        Instant inicializadoSistema = looca.getSistema().getInicializado();


        String sql = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'modelo do Sistema';
        
                """.formatted(
                modeloSistema,
                fkMaquina,
                1

        );

        con.executarQuery(sql);

        String sql22 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%d' and fkTipoComponente = '%d' and nomeCampo = 'inicialização do sistema';
                """.formatted(
                inicializadoSistema,
                fkMaquina,
                1);

        con.executarQuery(sql22);

        if (servidor){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(sql);
            con1.executarQuery(sql22);

        }

    }
}
