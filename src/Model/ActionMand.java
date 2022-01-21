package Model;

import java.util.concurrent.RecursiveAction;

public class ActionMand extends RecursiveAction {

    private final int from, to;
    private final double[][] data;
    private final double pas;
    private final double x;
    private final double y;
    private final Fonction f;
    private final int iter;
    private final int min;
    private final int rad;

    public ActionMand(int f, int t, double[][] d, double pas, double x, double y, Fonction fo, int i, int min, int rad){
        from = f;
        to = t;
        data = d;
        this.pas = pas;
        this.x = x;
        this.y = y;
        this.f = fo;
        iter = i;
        this.min = min;
        this.rad = rad;
    }

    @Override
    protected void compute() {
        if (to - from <= min) {
            computeDirectly();
            return;
        }
        int middle = (from + to) / 2;
        invokeAll(new ActionMand(from, middle, data, pas, x, y,f,iter,min,rad),
                new ActionMand(middle, to, data, pas, x, y,f,iter,min,rad));
    }

    private void computeDirectly(){
        int[] inds = calculeInd();
        int vi = inds[0];
        int vj = inds[1];
        int compt = 0;
        double mult = chercheMult(pas) + 3;
        while (compt < to) {
            for (int j = vj; j < data[0].length; j++) {
                Complex c = new Complex.Builder(Math.round((x + pas*j)*mult)/mult, Math.round((y + pas*vi)*mult)/mult).build();
                data[vi][j] = divergenceIndex(c);
                compt++;
            }
            for (int i = vi+1; i < data.length; i++) {
                for (int j= 0; j < data[0].length ;j++) {
                    Complex c = new Complex.Builder(Math.round((x + pas*j)*mult)/mult, Math.round((y + pas*i)*mult)/mult).build();
                    data[i][j] = divergenceIndex(c);
                    compt++;
                }
            }
        }
    }

    /**
     * Trouver le multiplicateur adequat
     * @param pas : pas de la fractale
     * @return un double permettant de cast un autre double selon les chiffres apres la virgule de pas
     */
    public double chercheMult(double pas){
        String val = String.valueOf(pas);
        if (val.length() < 3){
            return 1;
        } else {
            boolean virgule = false;
            double compt = 0.0;
            int i=0;
            while (i != val.length()){
                if (val.charAt(i) == '.'){
                    if (val.length() == i + 2){
                        if (val.charAt(i+1) == '0'){
                            return 1;
                        }
                    }
                    virgule = true;
                }
                else if (virgule){
                    compt++;
                }
                i++;
            }
            if (compt == 0){
                return 1;
            } else {
                return Math.pow(10.0,compt);
            }
        }
    }

    /**
     * Calcul l'indice de divergence
     * @param z0 : complexe initial
     * @return l'indice de divergence en fonction du complexe initial
     */
    public int divergenceIndex (Complex z0){
        int ite = 0;
        Complex zn = z0;
        Fonction fonction = new Fonction.BuilderFonction().coef(f.getCoeff()).cons(z0).build();
        while (ite < this.iter && zn.module() <= rad){
            zn = fonction.apply(zn);
            ite++;
        }
        return ite;
    }

    /**
     * Calcule les premiers indices de depart
     * @return un tableau avec le premier i et j correspondant a from
     */
    public int[] calculeInd(){
        int i = 0;
        int j = 0;
        int compt = 0;
        while (compt < from) {
            for (int a = 0; a < data.length; a++) {
                i = a;
                for (int b = 0; b < data[0].length; b++) {
                    j = b;
                    compt++;
                }
            }
        }
        return new int[]{i,j};
    }
}
