package Forca;

import ClienteServidor.Comunicado;

import java.util.ArrayList;

public class ControladorDePalavrasJaDigitadas extends Comunicado {
    private ArrayList<String> palavrasJaDigitadas;

    public ControladorDePalavrasJaDigitadas()
    {
        this.palavrasJaDigitadas = new ArrayList<String>();
    }

    public boolean isJaDigitada (String palavra)
    {
        int qtd = this.palavrasJaDigitadas.size();
        for(int pos = 0; pos < qtd; pos++ )
        {
            if(palavra.toUpperCase().equals(this.palavrasJaDigitadas.get(pos)))
            {
                return true;
            }
        }

        return false;
    }

    public void registre (String palavra) throws Exception
    {
        if(this.isJaDigitada(palavra))
            throw new Exception("Palavra já digitada!");

        this.palavrasJaDigitadas.add(palavra);
    }

    @Override
    public String toString ()
    {
        String ret = "";
        int qtd = this.palavrasJaDigitadas.size();
        for(int i = 0; i < qtd; i++)
        {
            if(i == qtd - 1)
            {
                ret += this.palavrasJaDigitadas.get(i);
            }
            else
                ret += this.palavrasJaDigitadas.get(i) + ",";
        }
        return ret;
    }
    @Override
    public boolean equals (Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(obj.getClass() != ControladorDePalavrasJaDigitadas.class)
            return false;

        ControladorDePalavrasJaDigitadas controladorDeLetrasJaDigitadas = (ControladorDePalavrasJaDigitadas) obj;

        if(this.palavrasJaDigitadas != controladorDeLetrasJaDigitadas.palavrasJaDigitadas)
            return false;

        return true;
    }
    @Override
    public int hashCode ()
    {
        int ret = 8;

        int qtd = this.palavrasJaDigitadas.size();

        for (String palavrasJaDigitada : this.palavrasJaDigitadas)
            ret = (29 * ret) + new String(palavrasJaDigitada).hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
        // calcular e retornar o hashcode de this
    }

    public ControladorDePalavrasJaDigitadas(
            ControladorDePalavrasJaDigitadas controladorDeLetrasJaDigitadas)
            throws Exception // construtor de c�pia
    {
        if(controladorDeLetrasJaDigitadas == null)
            throw new Exception("Modelo Ausente!");

        this.palavrasJaDigitadas = controladorDeLetrasJaDigitadas.palavrasJaDigitadas;
    }


    @Override
    public Object clone ()
    {
        ControladorDePalavrasJaDigitadas ret = null;
        try
        {
            ret = new ControladorDePalavrasJaDigitadas(this);
        }
        catch(Exception erro) {
            erro.printStackTrace();
        }

        return ret;
    }
}
