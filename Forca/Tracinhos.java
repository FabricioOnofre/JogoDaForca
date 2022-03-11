package Forca;

import ClienteServidor.Comunicado;

public class Tracinhos extends Comunicado implements Cloneable {

    private char texto [];

    public Tracinhos (int qtd) throws Exception
    {
        if(qtd < 0)
            throw new Exception("Quantidade negativa!");

        this.texto = new char [qtd];
        for(int ret = 0; ret < this.texto.length; ret++)
        {
            this.texto[ret] = '_';
        }
    }

    public void revele (int posicao, char letra) throws Exception
    {
        if(posicao < 0 || posicao >= this.texto.length)
            throw new Exception("Posição inválida!");

        this.texto[posicao] = letra;
    }

    public boolean isAindaComTracinhos ()
    {
        int i = 0;
        boolean b = false;

        while(b != true && i < this.texto.length)
        {
            if(this.texto[i] == '_')
                b = true;

            i++;
        }

        return b;
    }
    @Override
    public String toString ()
    {
        String ret = "";
        for(int i = 0; i < this.texto.length; i++)
        {
            ret += this.texto[i] + " ";
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

        if(obj.getClass() != Tracinhos.class)
            return false;

        Tracinhos tracinhos = (Tracinhos) obj;

        if(this.texto != tracinhos.texto)
            return false;

        return true;
    }
    @Override
    public int hashCode ()
    {
        int ret = 1;

        for(int i = 0; i < this.texto.length; i++)
            ret = (29 * ret) + new Character(this.texto[i]).hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
    }

    public Tracinhos (Tracinhos t) throws Exception // construtor de c�pia
    {
        if(t == null)
            throw new Exception("Modelo ausente!");

        this.texto = t.texto;
    }
    @Override
    public Object clone ()
    {
        Tracinhos ret = null;
        try
        {
            ret = new Tracinhos(this);
        }

        catch(Exception erro){}

        return ret;
    }

}
