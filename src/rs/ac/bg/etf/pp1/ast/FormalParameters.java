// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:35


package rs.ac.bg.etf.pp1.ast;

public class FormalParameters extends FormPars {

    private FParamDeclList FParamDeclList;

    public FormalParameters (FParamDeclList FParamDeclList) {
        this.FParamDeclList=FParamDeclList;
        if(FParamDeclList!=null) FParamDeclList.setParent(this);
    }

    public FParamDeclList getFParamDeclList() {
        return FParamDeclList;
    }

    public void setFParamDeclList(FParamDeclList FParamDeclList) {
        this.FParamDeclList=FParamDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FParamDeclList!=null) FParamDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FParamDeclList!=null) FParamDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FParamDeclList!=null) FParamDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParameters(\n");

        if(FParamDeclList!=null)
            buffer.append(FParamDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameters]");
        return buffer.toString();
    }
}
