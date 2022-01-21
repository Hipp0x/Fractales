package Model;


public class BuilderFractal {

    protected double[][] rect;
    protected double pas;
    protected Fonction function;
    protected int iter;
    protected int color;
    protected String fic;
    protected int ordre;
    protected String type;
    protected int radius;

    public BuilderFractal(){
        this.rect = new double[][]{{-1, 1}, {-1, 1}};
        this.pas = 0.10;
        this.iter = 1000;
        this.color = 0;
        this.fic = "Fractale";
        this.ordre = 0;
        this.type = "";
        this.radius = 2;
    }

    public BuilderFractal rect(double[] r){
        this.rect = new double[][]{{r[0], r[1]}, {r[2], r[3]}};
        return this;
    }

    public BuilderFractal pas(double p){
        this.pas = p;
        return this;
    }

    public BuilderFractal fonction(Fonction f){
        this.function = f;
        return this;
    }

    public BuilderFractal fichier(String f){
        this.fic = f;
        return this;
    }

    public BuilderFractal coloration(int t){
        this.color = t;
        return this;
    }

    public BuilderFractal ordre(int t){
        this.ordre = t;
        return this;
    }

    public BuilderFractal type(String t){
        this.type = t;
        return this;
    }

    public BuilderFractal iter(int i){
        this.iter = i;
        return this;
    }

    public BuilderFractal radius(int i){
        this.radius = i;
        return this;
    }

    public Fractal build(){
        if (this.type.equals("J")) {
            return new Julia(this);
        } else if (this.type.equals("M")){
            return new Mandelbrot(this);
        } else {
            return new Sierpinski(this);
        }
    }

}
