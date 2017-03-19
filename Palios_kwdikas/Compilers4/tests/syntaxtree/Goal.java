//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package syntaxtree;

/**
 * Grammar production:
 * f0 -> "MAIN"
 * f1 -> StmtList()
 * f2 -> "END"
 * f3 -> ( Procedure() )*
 * f4 -> <EOF>
 */
public class Goal implements Node {
   public NodeToken f0;
   public StmtList f1;
   public NodeToken f2;
   public NodeListOptional f3;
   public NodeToken f4;

   public Goal(NodeToken n0, StmtList n1, NodeToken n2, NodeListOptional n3, NodeToken n4) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
   }

   public Goal(StmtList n0, NodeListOptional n1) {
      f0 = new NodeToken("MAIN");
      f1 = n0;
      f2 = new NodeToken("END");
      f3 = n1;
      f4 = new NodeToken("");
   }

   public void accept(visitor.Visitor v) throws Exception {
      v.visit(this);
   }
   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) throws Exception {
      return v.visit(this,argu);
   }
   public <R> R accept(visitor.GJNoArguVisitor<R> v) throws Exception {
      return v.visit(this);
   }
   public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) throws Exception {
      v.visit(this,argu);
   }
}

