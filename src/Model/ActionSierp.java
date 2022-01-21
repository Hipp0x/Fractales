package Model;

import java.util.concurrent.RecursiveAction;

public class ActionSierp extends RecursiveAction {

    int ordre;
    double[][] tab;
    int compt;
    int i1;
    int i2;
    int j1;
    int j2;

    public ActionSierp(int o, double[][] t, int c, int i1, int i2, int j1, int j2){
        ordre = o;
        tab = t;
        compt = c;
        this.i1 = i1;
        this.i2 = i2;
        this.j1 = j1;
        this.j2 = j2;
    }

    @Override
    protected void compute() {
        if (compt < this.ordre){
            int li1 = i1 + (i2 - i1)/3;
            int li2 = i1 + 2*(i2 - i1)/3;
            if (i2 - i1 == 3){
                li1 = i1 + 1;
                li2 = i1 + 2;
            }

            int lj1 = j1 + (j2 - j1)/3;
            int lj2 = j1 +2*(j2 - j1)/3;
            if (j2 - j1 == 3){
                lj1 = j1 + 1;
                lj2 = j1 + 2;
            }
            computeDirectly(li1,li2,lj1,lj2);
            int nv = compt + 1;
            invokeAll(new ActionSierp(ordre,tab,nv,i1,li1,j1,lj1),
                    new ActionSierp(ordre,tab,nv,i1,li1,lj1,lj2),
                    new ActionSierp(ordre,tab,nv,i1,li1,lj2,j2),
                    new ActionSierp(ordre,tab,nv,li1,li2,j1,lj1),
                    new ActionSierp(ordre,tab,nv,li1,li2,lj2,j2),
                    new ActionSierp(ordre,tab,nv,li2,i2,j1,lj1),
                    new ActionSierp(ordre,tab,nv,li2,i2,lj1,lj2),
                    new ActionSierp(ordre,tab,nv,li2,i2,lj2,j2));
        }
    }

    private void computeDirectly(int li1, int li2, int lj1, int lj2){
        for (int i = li1; i < li2; i++) {
            for (int j = lj1; j < lj2; j++) {
                tab[i][j] = 1;
            }
        }
    }
}
