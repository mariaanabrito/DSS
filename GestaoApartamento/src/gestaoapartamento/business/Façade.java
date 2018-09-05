package gestaoapartamento.business;

import gestaoapartamento.data.ComprovativoDAO;
import gestaoapartamento.data.InquilinoDAO;
import gestaoapartamento.data.DespesaDAO;
import gestaoapartamento.presentation.InexistentUserException;
import gestaoapartamento.presentation.InvalidPasswordException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Façade {
   
    /*
        Variáveis de instância
    */
    
    private Inquilino sessao;
    
    private ComprovativoDAO _comprovativoDAO;
    private DespesaDAO _despesaDAO;
    private InquilinoDAO _inquilinoDAO;
    
    private final String convite = "1234";
    
    /*
        Construtor vazio
    */

    
    public Façade()
    {
        _comprovativoDAO = new ComprovativoDAO();
        _despesaDAO = new DespesaDAO();
        _inquilinoDAO = new InquilinoDAO();
        sessao = null;
    }
    
    
    /*
        Retorna a string que verifica se o inquilino se pode registar como utilizador. 
    */
    public String getConvite() 
    {
        return convite; 
    }
    
    /*
        Método que recebe o número de cartão de cidadão , o email, o número de telemóvel, o nome, a passworrd, e um booleano que indica se ele
    é admnistrador ou um simples inquilino e chama uma função do DAO que permite inserir na Base de Dados.
    */
    public void registaInquilino(int numCC, String email, int numT, String nome, String pass, boolean admin)
    {
        Conta c = new Conta(0, 0, numCC);
        Inquilino i;
        if (!admin)
        {
            i = new Inquilino(numCC, email, numT, nome, pass, c);
        }
        else
        {
            i = new Administrador(numCC, email, numT, nome, pass, c);
        }
        
        _inquilinoDAO.put(numCC, i);
    }
    
    /*
        Método que recebe um inquilino e chama um método do DAO que o atualiza na base de dados.
    */
    public void atualizaInquilino(Inquilino i)
    {
        _inquilinoDAO.put(i.getNumCC(), i);
        sessao = _inquilinoDAO.get(i.getNumCC());
    }
    
    /*
        Método que se encarrega de fazer o login carregando os dados necessários para a variável sessão.
    */
    public void login(int numcc, String pass) throws InexistentUserException, InvalidPasswordException
    {
        if (!_inquilinoDAO.containsKey(numcc))
            throw new InexistentUserException("Inquilino inexistente!");
        else
        {
           Inquilino i = _inquilinoDAO.get(numcc);
           
           if(!i.getPassword().equals(pass))
               throw new InvalidPasswordException("Inquilino inexistente!");
           else
           {
               sessao = i;
           }
        }
    }
    
    /*
        Método que se encarrega de fazer logout.
    */
    public void logout()
    {
        sessao = null;
    }
    
    /*
        Método que verifica se existe um inquilino na base de dados.
    */
    public boolean containsInquilino(int num)
    {
        return _inquilinoDAO.containsKey((Integer) num);
    }
    
    /*
        Método que verifica se o utilizador com a sessão iniciado é administrador.
    */
    public boolean isAdmin()
    {
        boolean res = false;
        if (sessao instanceof Administrador)
            res = true;
        return res;
    }
    
    /*
        Método que retorna um array de strings que contem a representação textual de todas as despesas.
    */
    public String[] getDespesas()
    {
        int i = 0;
        String[] despesas;
        Set<String> aux = _despesaDAO.keySet();
        despesas = new String[aux.size()];
        
        for(String s: aux)
        {
            despesas[i++] = s;
        }
            
        return despesas;
    }
    
    /*
        Método que recebe uma despesa(STRING) e retorna um float correspondente a essa despesa.
    */
    public float getValorDespesa(String key)
    {
        Despesa d = _despesaDAO.get(key);
        
        return d.getMontante();
    }
    
    /*
        Método que recebe uma despesa e um montante e chama um método do DAO que
    insere uma despesa na base de dados.
    */
    public void insereDespesa(String key, float montante)
    {   float antiga_divida;
        if (_despesaDAO.containsKey(key))
            antiga_divida = _despesaDAO.get(key).getMontante();
        else antiga_divida = 0;
        Despesa d = new Despesa(key, montante);
        _despesaDAO.put(key, d);
        updateDividas(key, antiga_divida, montante);
        
    }
    
    public void updateDividas(String despesa, float antiga, float nova) {
        float nova_divida;
        
        
        for(Inquilino i : _inquilinoDAO.values()) {
            nova_divida = 0;
           
            for(Taxa t : _inquilinoDAO.getTaxas(i.getNumCC()).values()) {
                
                float dividaAtual = _inquilinoDAO.getConta(i.getNumCC()).getDivida();
                if(t.getDespesa().equals(despesa))
                {
                    if(antiga < nova)
                    {
                        dividaAtual += (t.getTaxa()/100) * (nova-antiga);

                    }
                    if(antiga > nova)
                    {
                        dividaAtual -= (t.getTaxa()/100)*(antiga - nova);
                    }
                }
                if(dividaAtual < 0)
                    dividaAtual = 0;
            _inquilinoDAO.updateDivida(i.getNumCC(), dividaAtual);
            if (sessao.getNumCC() == i.getNumCC())
                sessao.getConta().setDivida(dividaAtual);
        }
    }
}
    
     /*
        Método que recebe uma despesa (string) e retorna um float correspondente
    ao  valor da taxa.
    */
    public float getValorTaxa(String key)
    {
        int numCC = sessao.getNumCC();
        Map<String, Taxa> map = _inquilinoDAO.getTaxas(numCC);
        Taxa t = map.get(key);
        return t == null ? 0 : t.getTaxa();     
    }
    
     /*
        Método que rece uma despesa(string), uma taxa(float), e um número de CC(
    int) e atualiza a taxa, chamando um método do DAO que a insere na base de 
    dados.
    */
    public void insereTaxa(String key, float taxa, int numCC)
    {
        float divida = _inquilinoDAO.get(numCC).getConta().getDivida();
        float d = _despesaDAO.get(key).getMontante();
        Map<String, Taxa> map = _inquilinoDAO.getTaxas(numCC);
        if(map.containsKey(key))
        {
            Taxa t = map.get(key);
            float valor = t.getTaxa();
            float taxaFinal;
            if(valor < taxa)
            {
                taxaFinal = (taxa-valor)/100;
                divida += taxaFinal*d;
            }
            else
            {
                taxaFinal = (valor-taxa)/100;
                divida -= taxaFinal*d;
            }
        }
        else
        {
            divida += (taxa/100)*d;
        }
        int n = _inquilinoDAO.get(numCC).getConta().getNumPrestacoes();
        Conta c = new Conta(divida, n, numCC);
        _inquilinoDAO.putConta(numCC, c);
        _inquilinoDAO.putTaxa(numCC, key, taxa);
        if(numCC == sessao.getNumCC())
            sessao.getConta().setDivida(divida);
    }
    
     /*
        Método que retorna um map com o número de CC dos inquilinos e o seu nome.
    */
    public Map<Integer, String> getInquilinos()
    {
        Map<Integer, String> map = new HashMap<>();
                for(Inquilino i :  _inquilinoDAO.values())
                    map.put(i.getNumCC(), i.getNome());
        return map;
    }
    
     /*
        Método que recebe um número de cartão de cidadão e retorna o número de 
    prestações que ele tem de pagar.
    */
    public int getNPrestacoes(int numCC)
    {
        Conta c = _inquilinoDAO.getConta(numCC);
        return c.getNumPrestacoes();
    }
    
    /*
        Método que recebe um número de CC, um número de prestações e chama um 
    método do DAO que insere esse número na base de dados.
    */
    public void insereNPrestacoes(int numCC, int nprest)
    {
        _inquilinoDAO.updateNumeroPrestacoes(numCC, nprest);
        if(numCC == sessao.getNumCC())
            sessao.getConta().setNumPrestacoes(nprest);
    }
    
     /*
        Método que verifica se existe um determinado tipo de despesa.
    */
    public boolean existeDespesa(String desig)
    {
        return _despesaDAO.containsKey(desig);
    }
    
     /*
        Método que retorna o inquilino com a sessão iniciada.
    */
    public Inquilino getSessao()
    {
        return sessao;
    }
    
     /*
        Método que recebe um número de CC e chama um método do DAO que o remove
    da base de dados.
    */
    public void removeInquilino(int numcc)
    {
        _inquilinoDAO.remove(numcc);
    }
    
     /*
        Método que retorna um array de strings com a representação textual dos inquilinos em dívida.
    */
    public String[] getInquilinosEmDivida()
    {
        String[] s = new String[_inquilinoDAO.size()];
        int ind = 0;
        for(Inquilino i: _inquilinoDAO.values())
        {
            if (i.getConta().getDivida() > 0)
                s[ind++] = i.getNumCC() + " - " + i.getNome() + " - " + i.getConta().getDivida() + "€";
        }
        
        return s;
    }
    
     /*
        Método que retorna a dívida total do apartamento.
    */
    public float getDividaTotal()
    {
        float f = 0;
        for(Inquilino i : _inquilinoDAO.values())
        {
            f += i.getConta().getDivida();
        }
        return f;
    }
    
     /*
        Método que retorna um array de strings com a representação textual dos inquilinos.
    */
    public String[] getInquilinosApartamento()
    {
        String[] s = new String[_inquilinoDAO.size()];
        int j = 0;
        for(Inquilino i: _inquilinoDAO.values())
        {
            s[j++] = i.getNumCC() + " - " + i.getNome();
        }
        
        return s;
    }
    
    /*
        Método que recebe um número de CC e uma data e chama um método do DAO 
    que insere esse comprovativo na base de dados.
    */
    public void insereComprovativo(int numcc, String data)
    {
        Inquilino i = _inquilinoDAO.get(numcc);
        Conta conta = i.getConta();
        
        int ct = conta.getIDConta();   
        int nprest = conta.getNumPrestacoes();
        float divida = conta.getDivida();
        
        float montante = divida/nprest;
        
        divida -= montante;
        if(nprest > 0)
        {
            nprest--;
        }
                
        Comprovativo c = new Comprovativo(0, montante, data, ct);
        
        _comprovativoDAO.put(null, c);
        _inquilinoDAO.updateDivida(numcc, divida);
        _inquilinoDAO.updateNumeroPrestacoes(numcc, nprest);
        if(numcc == sessao.getNumCC())
        {
            sessao.getConta().setDivida(divida);
            sessao.getConta().setNumPrestacoes(nprest);
        }
    }
    
    /*
        Método que retorna o número de comprovativos na base de dados.
    */
    public int getSizeComprovativos()
    {
        return _comprovativoDAO.size();
    }
    
    /*
        Método que retorna um array de strings com a representação textual de todos os comprovativos.
    */
    public String[] getTotalComprovativos()
    {
        String[] s = new String[getSizeComprovativos()];
        int i = 0;
        
        for(Comprovativo c : _comprovativoDAO.values())
        {
            s[i++] = c.toString();
        }
        return s;
    }
    
    /*
        Método que retorna um array de strings com a representação textual dos inquilinos dependendo do número de CC que o método recebe.
    */
    public String[] getComprovativosInquilino(int num)
    {
        String[] s = new String[getSizeComprovativos()];
        int i = 0;
        
        for(Comprovativo c : _comprovativoDAO.values())
        {
            if(c.getIdConta() == num)
                s[i++] = c.toString();
        }
        return s;
    }
    
    /*
        Método que retorna um array de strings com a representação textual de todas as despesas.
    */
    public String[] getDespesasApartamento()
    {
        String[] s = new String[_despesaDAO.size()];
        int i = 0;
        
        for(Despesa d: _despesaDAO.values())
        {
            s[i++] = d.toString();
        }
        
        return s;
    }
    
    /*
        Método que retorna um array de strings com a representação textual das 
    do inquilino com a sessão iniciada.
    */
    public String[] getTaxasPessoais()
    {
        String[] s = new String[_despesaDAO.size()];
        int i = 0;
        for(Taxa t: _inquilinoDAO.getTaxas(sessao.getNumCC()).values())
        {
            s[i++] = t.toString();
        }
        return s;
    }   
}
