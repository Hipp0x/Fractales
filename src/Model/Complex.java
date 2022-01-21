package Model;

public class Complex {

    private final double p_reel;
    private final double p_img;

    public static class Builder {

        private final double p_reel;
        private final double p_img;

        public Builder (double r, double i){
            p_img = i;
            p_reel = r;
        }

        public Complex build(){
            return new Complex(this);
        }
    }

    private Complex( Builder builder){
        p_img = builder.p_img;
        p_reel = builder.p_reel;
    }

    @Override
    public String toString() {
        return p_reel + " + i"+ p_img;
    }

    public double getP_reel() {
        return p_reel;
    }

    public double getP_img() {
        return p_img;
    }

    /*
     * ***************************************************** *
     *                     Operations
     * ***************************************************** *
     */

    /**
     * Additionne deux complexes
     * @param b : le complexe qu'on souhaite additionner
     * @return l'addition de notre complexe et b
     */
    public Complex add(Complex b){
        return new Complex( new Builder( (this.p_reel + b.p_reel), (this.p_img + b.p_img) ));
    }

    /**
     * Multiplie deux complexes
     * @param b : le complexe qu'on souhaite multiplier
     * @return la multiplication entre notre complexe et b
     */
    public Complex mul(Complex b){
        double pr = (this.p_reel * b.p_reel) - (this.p_img * b.p_img);
        double pi = ((this.p_reel + this.p_img)*(b.p_reel + b.p_img)) -
                (this.p_reel*b.p_reel) - (this.p_img * b.p_img);
        return new Complex( new Builder( pr, pi ));
    }

    /**
     * Divise deux complexes
     * @param b : le complexe au denominateur
     * @return la division de notre complexe par b
     */
    public Complex div(Complex b){
        if (b.p_img == 0){
            return new Complex(new Builder( this.p_reel / b.p_reel, this.p_img / b.p_reel ));
        } else {
            //System.out.println("this : "+this);
            //System.out.println("b : "+b);
            Complex c = new Complex.Builder(b.p_reel, -b.p_img).build();
            Complex num = this.mul(c);
            //System.out.println("num : "+num);
            Complex den = new Complex.Builder(b.p_reel*b.p_reel + b.p_img*b.p_img,0).build();
            //System.out.println("den : "+den);
            return new Complex(new Builder(num.p_reel / den.p_reel, num.p_img/den.p_reel));
        }
    }

    /**
     * Calcule le module du complexe
     * @return le module du complexe
     */
    public double module(){
        return Math.sqrt( (this.p_reel * this.p_reel) + (this.p_img * this.p_img));
    }

    /**
     * Met un complexe sous une puissance
     * @param c : complexe initial
     * @param puissance : puissance voulue
     * @return le complexe c passé a la puissance voulue
     */
    public static Complex puissance(Complex c, double puissance){
        if (puissance == 1 ){
            return c;
        } else {
            Complex res = c;
            for (double i = 1; i < puissance; i += 1) {
                res=res.mul(c);
            }
            return res;
        }
    }

    public Complex cos(){
        return new Complex.Builder(Math.cos(p_reel) * Math.cosh(p_img), - (Math.sin(p_reel) * Math.sinh(p_img))).build();
    }

    public Complex sin(){
        return new Complex.Builder(Math.sin(p_reel) * Math.cosh(p_img), - (Math.cos(p_reel) * Math.sinh(p_img))).build();
    }

    public Complex sinh(){
        // (e puiss x - e puissance-x) / 2
        return new Complex.Builder(Math.sinh(p_reel)*Math.cos(p_img),  Math.cosh(p_reel)*Math.sin(p_img)).build();
    }

}
