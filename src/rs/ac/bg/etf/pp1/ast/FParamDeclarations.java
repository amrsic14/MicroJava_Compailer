// generated with ast extension for cup
// version 0.8
// 23/5/2020 15:34:22


package rs.ac.bg.etf.pp1.ast;

public class FParamDeclarations extends FParamDeclList {

    private FParamDeclList FParamDeclList;
    private FParamDeclaration FParamDeclaration;

    public FParamDeclarations (FParamDeclList FParamDeclList, FParamDeclaration FParamDeclaration) {
        this.FParamDeclList=FParamDeclList;
        if(FParamDeclList!=null) FParamDeclList.setParent(this);
        this.FParamDeclaration=FParamDeclaration;
        if(FParamDeclaration!=null) FParamDeclaration.setParent(this);
    }

    public FParamDeclList getFParamDeclList() {
        return FParamDeclList;
    }

    public void setFParamDeclList(FParamDeclList FParamDeclList) {
        this.FParamDeclList=FParamDeclList;
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
        if(FParamDeclList!=null) FParamDeclList.accept(visitor);
        if(FParamDeclaration!=null) FParamDeclaration.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FParamDeclList!=null) FParamDeclList.traverseTopDown(visitor);
        if(FParamDeclaration!=null) FParamDeclaration.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FParamDeclList!=null) FParamDeclList.traverseBottomUp(visitor);
        if(FParamDeclaration!=null) FParamDeclaration.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FParamDeclarations(\n");

        if(FParamDeclList!=null)
            buffer.append(FParamDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FParamDeclaration!=null)
            buffer.append(FParamDeclaration.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FParamDeclarations]");
        return buffer.toString();
    }
}
