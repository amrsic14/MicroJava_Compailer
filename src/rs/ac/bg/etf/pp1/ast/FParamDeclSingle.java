// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:35


package rs.ac.bg.etf.pp1.ast;

public class FParamDeclSingle extends FParamDeclList {

    private FParamDeclaration FParamDeclaration;

    public FParamDeclSingle (FParamDeclaration FParamDeclaration) {
        this.FParamDeclaration=FParamDeclaration;
        if(FParamDeclaration!=null) FParamDeclaration.setParent(this);
    }

    public FParamDeclaration getFParamDeclaration() {
        return FParamDeclaration;
    }

    public void setFParamDeclaration(FParamDeclaration FParamDeclaration) {
        this.FParamDeclaration=FParamDeclaration;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FParamDeclaration!=null) FParamDeclaration.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FParamDeclaration!=null) FParamDeclaration.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FParamDeclaration!=null) FParamDeclaration.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FParamDeclSingle(\n");

        if(FParamDeclaration!=null)
            buffer.append(FParamDeclaration.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FParamDeclSingle]");
        return buffer.toString();
    }
}
