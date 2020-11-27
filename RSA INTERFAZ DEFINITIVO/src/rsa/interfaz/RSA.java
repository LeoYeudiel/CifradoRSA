/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa.interfaz;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import javax.swing.*;
import jdk.jfr.events.FileWriteEvent;

/**
 *
 * @author hp
 */
public class RSA {
    int tamPrimo;
    JTextArea msjcifra, decifratxt, cifradotxt;
    BigInteger p,q,n,phi,d,e;
    JTextField ptext,qtext,phitext,ntext, etext, dtext;    
    
    //construtor
    public RSA(int cant,JTextField pvar, JTextField qvar, JTextField phivar, JTextField nvar, JTextField evar, JTextArea cifradovar){
        this.tamPrimo=cant;
        this.ptext=pvar;
        this.qtext=qvar;
        this.phitext=phivar;
        this.ntext=nvar;
        this.etext=evar;
        this.cifradotxt=cifradovar;
    }
    
    public RSA(JTextField nvar, JTextField phivar,JTextField dvar, JTextArea msjcifravar, JTextArea decifravar,JTextField evar){
        this.phitext=phivar;
        this.ntext=nvar;
        this.dtext=dvar;
        this.msjcifra=msjcifravar;
        this.decifratxt=decifravar;
        this.etext=evar;
   }
    
    //metodo que se encarga de generar primos
    public void generarPrimos(){
    
        p = new BigInteger(tamPrimo,10,new Random());
        ptext.setText(""+p);
        // hasta que encuentre un primo
        do q = new BigInteger(tamPrimo,10, new Random());
        //compara que los numeros primos existan
       
            while(q.compareTo(p)==0);
        qtext.setText(""+q);
    }
    
    //generar las claves
    public void generarClaves(){
    
    //n =p*1
    //multiply es metodo de Integer para multiplicar
    n = p.multiply(q);
    
    
    //phi= (p-1)*(q-1)
    phi=p.subtract(BigInteger.valueOf(1));
    phi=phi.multiply(q.subtract(BigInteger.valueOf(1)));
    phitext.setText(""+phi);
    //calcular el primo relativo o coprimo e y menos que n
    
    do e = new BigInteger(2*tamPrimo, new Random());
    //calcular el minimo comun divisor de "e"
            while ((e.compareTo(phi)!=-1)||(e.gcd(phi).compareTo(BigInteger.valueOf(1))!=0));
        //calcular d
        etext.setText(""+e);
        ntext.setText(""+n);
        d=e.modInverse(phi);
    
    }
    
    public void generarD(){
       
    n=new BigInteger(ntext.getText());
    phi=new BigInteger(phitext.getText());
    e=new BigInteger(etext.getText());
        d=e.modInverse(phi);
        dtext.setText(""+d);
    }

    //cifrar
    public BigInteger[] encriptar(String mensaje){
        e=new BigInteger(etext.getText());
        n=new BigInteger(ntext.getText());
        //variables
        int i;
        byte[] temp= new byte[1];
        byte[] digitos = mensaje.getBytes();
        BigInteger[] bigdigitos = new BigInteger[digitos.length];
        //primero es recorrer el tamaño de bigdigitos para asignarlos al temp
        for (i=0; i<digitos.length; i++) {
            temp[0] = digitos[i];
            bigdigitos[i]= new BigInteger(temp);
            
        }
        
        //necesito un biginteger para el cifrado
        BigInteger[] cifrado = new BigInteger[bigdigitos.length];
        
        for (i=0; i<bigdigitos.length; i++) {
            //aplico el modulo para el cifrado
            cifrado[i] = bigdigitos[i].modPow(e, n);
        }
        ntext.setText(""+n);
        return cifrado;
    }
    //descifrar
    public String descrifrar(BigInteger[] cifrado){
    d=new BigInteger(dtext.getText());
    n=new BigInteger(ntext.getText());
    
    BigInteger[] descifrado = new BigInteger[cifrado.length];
    
    //descifrar
        for (int i=0; i<descifrado.length; i++) {
            //aplico el descifrado
            descifrado[i] = cifrado[i].modPow(d, n);
        }
        
        //lo tengo que colocar en un arreglo de caracteres para despues pasarlo a una cadena
        char[] charArray = new char [descifrado.length];
        for (int i = 0; i < charArray.length; i++) {
            charArray[i]=(char)(descifrado[i].intValue());
            
            
        }
        
        return (new String(charArray));
    }
    
    public void empaquetar(){
        for (int i=0; i<6;i++){
            FileWriter fichero=null;
            PrintWriter escribe=null;
            try{
                if (i==0) {
                    fichero =new FileWriter("cifrado.txt");
                    escribe = new PrintWriter(fichero);
                    escribe.println(cifradotxt.getText());
                }else if (i==2) {
                    fichero =new FileWriter("nfichero.txt");
                    escribe = new PrintWriter(fichero);
                    escribe.println(ntext.getText());
                }else if (i==3) {
                    fichero =new FileWriter("phifichero.txt");
                    escribe = new PrintWriter(fichero);
                    escribe.println(phitext.getText());
                }else if (i==4) {
                    fichero =new FileWriter("efichero.txt");
                    escribe = new PrintWriter(fichero);
                    escribe.println(etext.getText());
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void desempaquetar(){
        for (int i = 0; i < 8; i++) {
            String texto="";
            File fichero=null;
            FileReader fr=null;
            BufferedReader br=null;
            try {
                if (i==0) {
                    fichero =new File("cifrado.txt");
                    fr = new FileReader(fichero);
                    br = new BufferedReader(fr);
                    while ((texto=br.readLine()) !=null) 
                           msjcifra.setText(texto);
                    
                }else if (i==2) {
                    fichero =new File("nfichero.txt");
                    fr = new FileReader(fichero);
                    br = new BufferedReader(fr);
                    while ((texto=br.readLine()) !=null) 
                           ntext.setText(texto);
                    
                }else if (i==3) {
                    fichero =new File("phifichero.txt");
                    fr = new FileReader(fichero);
                    br = new BufferedReader(fr);
                    while ((texto=br.readLine()) !=null) 
                           phitext.setText(texto);
                    
                }else if (i==4) {
                    fichero =new File("efichero.txt");
                    fr = new FileReader(fichero);
                    br = new BufferedReader(fr);
                    while ((texto=br.readLine()) !=null) 
                           etext.setText(texto);  
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    if (null != fr) {
                        fr.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }        
        generarD();        
    }    
}