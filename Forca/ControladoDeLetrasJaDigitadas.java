package Forca;

import ClienteServidor.Comunicado;

public class ControladoDeLetrasJaDigitadas extends Comunicado implements Cloneable
{
    private String letrasJaDigitadas;

    public ControladoDeLetrasJaDigitadas()
    {
        this.letrasJaDigitadas = "";
    }

    public boolean isJaDigitada (char letra)
    {
        boolean b = false;
        int i = 0;
        while(b != true && i<this.letrasJaDigitadas.length())
        {
            if(letra == this.letrasJaDigitadas.charAt(i))
            {
                b = true;
            }
            i++;
        }
        return b;
    }

    public void registre (char letra) throws Exception
    {
        if(this.isJaDigitada(letra) == true)
            throw new Exception("Letra já digitada!");

        this.letrasJaDigitadas += letra;
    }
    @Override
    public String toString ()
    {
        String ret = "";
        for(int i = 0; i <this.letrasJaDigitadas.length(); i++)
        {
            ret += this.letrasJaDigitadas.charAt(i) + ",";
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

        if(obj.getClass() != ControladoDeLetrasJaDigitadas.class)
            return false;

        ControladoDeLetrasJaDigitadas controladorDeLetrasJaDigitadas = (ControladoDeLetrasJaDigitadas) obj;

        if(this.letrasJaDigitadas != controladorDeLetrasJaDigitadas.letrasJaDigitadas)
            return false;

        return true;
    }
    @Override
    public int hashCode ()
    {
        int ret = 8;
        ret = 19 * ret + new Integer(this.letrasJaDigitadas).hashCode();

        if(ret < 0)
            ret = -ret;

        return ret;
        // calcular e retornar o hashcode de this
    }

    public ControladoDeLetrasJaDigitadas(
            ControladoDeLetrasJaDigitadas controladorDeLetrasJaDigitadas)
            throws Exception // construtor de c�pia
    {
        if(controladorDeLetrasJaDigitadas == null)
            throw new Exception("Modelo Ausente!");

        this.letrasJaDigitadas = controladorDeLetrasJaDigitadas.letrasJaDigitadas;
    }
    @Override
    public Object clone ()
    {
        ControladoDeLetrasJaDigitadas ret = null;
        try
        {
            ret = new ControladoDeLetrasJaDigitadas(this);
        }
        catch(Exception erro) {}

        return ret;
    }
}