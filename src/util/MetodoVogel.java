/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author coloc
 */

import java.util.*;


public class MetodoVogel {

    public static class Paso {
        public int i, j;                 // índices usados en la matriz resuelta (puede haber dummy)
        public int iOriginal, jOriginal; // índices originales; -1 si es dummy
        public int asign;                // cantidad asignada
        public int costo;                // costo unitario
        public Paso(int i,int j,int iO,int jO,int asign,int costo){
            this.i=i; this.j=j; this.iOriginal=iO; this.jOriginal=jO; this.asign=asign; this.costo=costo;
        }
    }

    public static class Resultado {
        public List<Paso> pasos = new ArrayList<>();
        public int costoTotal = 0;
        public boolean seAgregoDummyFila = false;
        public boolean seAgregoDummyCol  = false;
        public int filasOriginales;
        public int colsOriginales;
    }

    /** Ejecuta Vogel. Si oferta≠demanda, balancea con dummy (costo 0). */
    public static Resultado resolver(int[][] costosIn, int[] ofertaIn, int[] demandaIn) {
        int m0 = ofertaIn.length, n0 = demandaIn.length;
        int sumaO=0,sumaD=0; for(int v:ofertaIn)sumaO+=v; for(int v:demandaIn)sumaD+=v;

        int[][] costos; int[] oferta; int[] demanda;
        Resultado R = new Resultado(); R.filasOriginales=m0; R.colsOriginales=n0;

        if (sumaO == sumaD) {
            costos = copiar(costosIn); oferta = Arrays.copyOf(ofertaIn, m0); demanda = Arrays.copyOf(demandaIn, n0);
        } else if (sumaO < sumaD) { // falta oferta -> fila dummy
            int faltante = sumaD - sumaO;
            costos = new int[m0+1][n0];
            for(int i=0;i<m0;i++) System.arraycopy(costosIn[i],0,costos[i],0,n0);
            Arrays.fill(costos[m0], 0);
            oferta = Arrays.copyOf(ofertaIn, m0+1); oferta[m0]=faltante;
            demanda = Arrays.copyOf(demandaIn, n0);
            R.seAgregoDummyFila = true;
        } else { // falta demanda -> columna dummy
            int faltante = sumaO - sumaD;
            costos = new int[m0][n0+1];
            for(int i=0;i<m0;i++){ System.arraycopy(costosIn[i],0,costos[i],0,n0); costos[i][n0]=0; }
            oferta = Arrays.copyOf(ofertaIn, m0);
            demanda = Arrays.copyOf(demandaIn, n0+1); demanda[n0]=faltante;
            R.seAgregoDummyCol = true;
        }

        int m = oferta.length, n = demanda.length;
        boolean[] filaAct = new boolean[m], colAct = new boolean[n];
        Arrays.fill(filaAct,true); Arrays.fill(colAct,true);

        int[] mapI = new int[m], mapJ = new int[n];
        for(int i=0;i<m;i++) mapI[i]=(i<R.filasOriginales)?i:-1;
        for(int j=0;j<n;j++) mapJ[j]=(j<R.colsOriginales)?j:-1;

        while(true){
            if (contar(filaAct)==0 || contar(colAct)==0) break;

            int[] penF = new int[m]; int[] minF = new int[m];
            Arrays.fill(penF,Integer.MIN_VALUE); Arrays.fill(minF,Integer.MAX_VALUE);
            for(int i=0;i<m;i++) if (filaAct[i]){
                int min1=Integer.MAX_VALUE,min2=Integer.MAX_VALUE;
                for(int j=0;j<n;j++) if (colAct[j]){
                    int c=costos[i][j];
                    if(c<min1){min2=min1;min1=c;} else if(c<min2){min2=c;}
                }
                if (min1==Integer.MAX_VALUE) continue;
                penF[i]=(min2==Integer.MAX_VALUE)?min1:(min2-min1);
                minF[i]=min1;
            }

            int[] penC = new int[n]; int[] minC = new int[n];
            Arrays.fill(penC,Integer.MIN_VALUE); Arrays.fill(minC,Integer.MAX_VALUE);
            for(int j=0;j<n;j++) if (colAct[j]){
                int min1=Integer.MAX_VALUE,min2=Integer.MAX_VALUE;
                for(int i=0;i<m;i++) if (filaAct[i]){
                    int c=costos[i][j];
                    if(c<min1){min2=min1;min1=c;} else if(c<min2){min2=c;}
                }
                if (min1==Integer.MAX_VALUE) continue;
                penC[j]=(min2==Integer.MAX_VALUE)?min1:(min2-min1);
                minC[j]=min1;
            }

            boolean porFila=true; int idx=-1; int maxPen=Integer.MIN_VALUE;
            for(int i=0;i<m;i++) if (filaAct[i] && penF[i]>maxPen){maxPen=penF[i]; idx=i; porFila=true;}
            for(int j=0;j<n;j++) if (colAct[j] && penC[j]>maxPen){maxPen=penC[j]; idx=j; porFila=false;}

            int bestI=-1,bestJ=-1,bestC=Integer.MAX_VALUE;
            if (porFila){
                int i=idx;
                for(int j=0;j<n;j++) if (colAct[j]){
                    int c=costos[i][j]; if(c<bestC){bestC=c; bestI=i; bestJ=j;}
                }
            } else {
                int j=idx;
                for(int i=0;i<m;i++) if (filaAct[i]){
                    int c=costos[i][j]; if(c<bestC){bestC=c; bestI=i; bestJ=j;}
                }
            }

            int asign = Math.min(oferta[bestI], demanda[bestJ]);
            R.pasos.add(new Paso(bestI,bestJ,mapI[bestI],mapJ[bestJ],asign,costos[bestI][bestJ]));
            R.costoTotal += asign * costos[bestI][bestJ];

            oferta[bestI]-=asign; demanda[bestJ]-=asign;
            if (oferta[bestI]==0) filaAct[bestI]=false;
            if (demanda[bestJ]==0) colAct[bestJ]=false;

            if (contar(filaAct)==1 || contar(colAct)==1){
                for(int i=0;i<m;i++) if (filaAct[i])
                    for(int j=0;j<n;j++) if (colAct[j])
                        if (oferta[i]>0 && demanda[j]>0){
                            int a=Math.min(oferta[i],demanda[j]);
                            R.pasos.add(new Paso(i,j,mapI[i],mapJ[j],a,costos[i][j]));
                            R.costoTotal += a*costos[i][j];
                            oferta[i]-=a; demanda[j]-=a;
                            if(oferta[i]==0)filaAct[i]=false;
                            if(demanda[j]==0)colAct[j]=false;
                        }
            }
        }
        return R;
    }

    // --------- Formatos de salida ---------
    public static String formatearDetalle(Resultado R){
        StringBuilder sb=new StringBuilder(); Locale us=Locale.US;
        for (Paso p: R.pasos){
            String si=(p.iOriginal>=0)?("S"+(p.iOriginal+1)):"Dummy";
            String dj=(p.jOriginal>=0)?("D"+(p.jOriginal+1)):"Dummy";
            int sub=p.asign*p.costo;
            sb.append(String.format(us,"%s - %s -> %d x %d = %,d%n",si,dj,p.asign,p.costo,sub));
        }
        if(R.seAgregoDummyFila||R.seAgregoDummyCol){
            sb.append("\n* Se agregó ");
            if(R.seAgregoDummyFila) sb.append("FILA dummy ");
            if(R.seAgregoDummyFila&&R.seAgregoDummyCol) sb.append("y ");
            if(R.seAgregoDummyCol) sb.append("COLUMNA dummy ");
            sb.append("para balancear.\n");
        }
        sb.append(String.format(us,"%nTotal = %,d", R.costoTotal));
        return sb.toString();
    }

    /** Estilo cuaderno: S? - costo x asign = subtotal (oculta dummies) */
    public static String formatearDetalleCorto(Resultado R){
        StringBuilder sb=new StringBuilder(); Locale us=Locale.US;
        for (Paso p: R.pasos){
            if (p.iOriginal<0) continue;
            String si="S"+(p.iOriginal+1); int sub=p.asign*p.costo;
            sb.append(String.format(us,"%s - %d x %d = %d%n", si, p.costo, p.asign, sub));
        }
        sb.append(String.format(us,"%nTotal = %d", R.costoTotal));
        return sb.toString();
    }

    // --------- Utils ---------
    private static int contar(boolean[] v){int k=0; for(boolean b:v) if(b)k++; return k;}
    private static int[][] copiar(int[][] a){int[][] r=new int[a.length][a[0].length];
        for(int i=0;i<a.length;i++) System.arraycopy(a[i],0,r[i],0,a[0].length); return r;}
}

