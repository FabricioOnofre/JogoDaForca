package Forca;

import ClienteServidor.Comunicado;

public class Palavra extends Comunicado implements Comparable<Palavra>
{

    private String texto;

    public Palavra (String texto) throws Exception
    {
        if(texto == null || texto == "")
            throw new Exception("Palavra inválida");

        this.texto = texto;
    }

    public int getQuantidade (char letra)
    {
        int qtd = 0;
        for (int i = 0; i<this.texto.length(); i++)
        {
            if(letra == this.texto.charAt(i))
            {
                qtd++;
            }
        }
        return qtd;
    }


    public int getPosicaoDaIezimaOcorrencia (int i, char letra) throws Exception
    {
        int ret = 0;

        for(int pos = 0; pos < this.texto.length(); pos++)
        {
            if(letra == this.texto.charAt(pos))
            {
                if(ret == i)
                    return pos;

                ret++;
            }
        }
        if(ret == 0)
        {
            throw new Exception("A letra não foi encontrada na palavra!");
        }

        throw new Exception("A Iezima posição da letra escolhida não foi encontrada!");
    }

    public int getTamanho ()
    {
        return this.texto.length();
    }
    @Override
    public String toString ()
    {
        return this.texto;
    }
    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;

        if (obj.getClass() != Palavra.class) return false;

        Palavra palavra = (Palavra) obj;

        if(this.texto != palavra.texto) return false;

        return true;
    }
    @Override
    public int hashCode ()
    {
        int ret = 1;
        ret = 17 * ret + new String(this.texto).hashCode();

        if(ret < 0) ret = -ret;
        return ret;
        // calcular e retornar o hashcode de this
    }
    @Override
    public int compareTo (Palavra palavra)
    {
        return this.texto.compareTo(palavra.texto);
    }

}
