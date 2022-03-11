package Forca;

import ClienteServidor.Comunicado;

public class ControladorDeErros extends Comunicado implements Cloneable{

    private int qtdMax, qtdErr=0;

    public ControladorDeErros(int qtdMax) throws Exception
    {
        if (qtdMax < 0)
            throw new Exception("Quantidade inválida");

        this.qtdMax = qtdMax;
    }

    public void registreUmErro() throws Exception
    {
        if(this.qtdErr == this.qtdMax)
            throw new Exception("Quantidade de erros máxima atingida");

        this.qtdErr++;
    }

    public boolean isAtingidoMaximoDeErros()
    {
        if(this.qtdErr == this.qtdMax)
            return true;

        else
            return false;
    }
    @Override
    public String toString()
    {
        return this.qtdErr + "/" + this.qtdMax;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;

        if(obj.getClass() != ControladorDeErros.class) return false;

        ControladorDeErros controladorDeErros = (ControladorDeErros)obj;

        if(this.qtdErr != controladorDeErros.qtdErr) return false;
        if(this.qtdMax != controladorDeErros.qtdMax) return false;

        return true;
    }
    @Override
    public int hashCode()
    {
        int ret = 3;

        ret = 23 * ret + new Integer(this.qtdErr).hashCode();
        ret = 5 * ret + new Integer(this.qtdMax).hashCode();

        if(ret < 0) ret = -ret;
        return ret;
    }

    public ControladorDeErros (ControladorDeErros c) throws Exception // construtor de c�pia
    {
        if(c == null)
            throw new Exception("Modelo ausente");

        this.qtdErr = c.qtdErr;
        this.qtdMax = c.qtdMax;
    }
    @Override
    public Object clone()
    {
        ControladorDeErros ret = null;
        try
        {
            ret = new ControladorDeErros(this);
        }
        catch(Exception erro) {}

        return ret;
    }

}
