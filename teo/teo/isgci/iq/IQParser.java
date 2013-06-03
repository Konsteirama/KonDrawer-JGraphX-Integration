//=========================================================================
//
//  This file was generated by Mouse 1.6 at 2013-06-03 13:15:35 GMT
//  from grammar
//    'E:\workspace\KonDrawer-JGraphX-Integration\teo\teo\isgci\iq\iq.peg'.
//    
//
//=========================================================================

package teo.isgci.iq;

import mouse.runtime.Source;

public class IQParser extends mouse.runtime.ParserTest
{
  final IQSemantics sem;
  
  //=======================================================================
  //
  //  Initialization
  //
  //=======================================================================
  //-------------------------------------------------------------------
  //  Constructor
  //-------------------------------------------------------------------
  public IQParser()
    {
      sem = new IQSemantics();
      sem.rule = this;
      super.sem = sem;
      caches = cacheList;
    }
  
  //-------------------------------------------------------------------
  //  Run the parser
  //-------------------------------------------------------------------
  public boolean parse(Source src)
    {
      super.init(src);
      sem.init();
      if (top()) return true;
      return failure();
    }
  
  //-------------------------------------------------------------------
  //  Get semantics
  //-------------------------------------------------------------------
  public IQSemantics semantics()
    { return sem; }
  
  //=======================================================================
  //
  //  Parsing procedures
  //
  //=======================================================================
  //=====================================================================
  //  top = space expr EOF {top} ~{bad} / _++ EOF ;
  //=====================================================================
  private boolean top()
    {
      if (saved(top)) return reuse();
      if (top_0())
      { sem.top(); return accept(top); }
      else sem.bad();
      if (top_1()) return accept(top);
      return reject(top);
    }
  
  //-------------------------------------------------------------------
  //  top_0 = space expr EOF
  //-------------------------------------------------------------------
  private boolean top_0()
    {
      if (savedInner(top_0)) return reuseInner();
      space();
      if (!expr()) return rejectInner(top_0);
      if (!EOF()) return rejectInner(top_0);
      return acceptInner(top_0);
    }
  
  //-------------------------------------------------------------------
  //  top_1 = _++ EOF
  //-------------------------------------------------------------------
  private boolean top_1()
    {
      if (savedInner(top_1)) return reuseInner();
      if (EOF()) return rejectInner(top_1);
      do if (!next($Term0)) return rejectInner(top_1);
        while (!EOF());
      return acceptInner(top_1);
    }
  
  //=====================================================================
  //  expr = term (or term)* {binop} ;
  //=====================================================================
  private boolean expr()
    {
      if (saved(expr)) return reuse();
      if (!term()) return reject(expr);
      while (expr_0());
      sem.binop();
      return accept(expr);
    }
  
  //-------------------------------------------------------------------
  //  expr_0 = or term
  //-------------------------------------------------------------------
  private boolean expr_0()
    {
      if (savedInner(expr_0)) return reuseInner();
      if (!or()) return rejectInner(expr_0);
      if (!term()) return rejectInner(expr_0);
      return acceptInner(expr_0);
    }
  
  //=====================================================================
  //  term = notfactor (and notfactor)* {binop} ;
  //=====================================================================
  private boolean term()
    {
      if (saved(term)) return reuse();
      if (!notfactor()) return reject(term);
      while (term_0());
      sem.binop();
      return accept(term);
    }
  
  //-------------------------------------------------------------------
  //  term_0 = and notfactor
  //-------------------------------------------------------------------
  private boolean term_0()
    {
      if (savedInner(term_0)) return reuseInner();
      if (!and()) return rejectInner(term_0);
      if (!notfactor()) return rejectInner(term_0);
      return acceptInner(term_0);
    }
  
  //=====================================================================
  //  notfactor = not? factor {not} ;
  //=====================================================================
  private boolean notfactor()
    {
      if (saved(notfactor)) return reuse();
      not();
      if (!factor()) return reject(notfactor);
      sem.not();
      return accept(notfactor);
    }
  
  //=====================================================================
  //  factor = rel {dup} / lparen expr (rparen / EOF) {paren} ;
  //=====================================================================
  private boolean factor()
    {
      if (saved(factor)) return reuse();
      if (rel())
      { sem.dup(); return accept(factor); }
      if (factor_0())
      { sem.paren(); return accept(factor); }
      return reject(factor);
    }
  
  //-------------------------------------------------------------------
  //  factor_0 = lparen expr (rparen / EOF)
  //-------------------------------------------------------------------
  private boolean factor_0()
    {
      if (savedInner(factor_0)) return reuseInner();
      if (!lparen()) return rejectInner(factor_0);
      if (!expr()) return rejectInner(factor_0);
      if (!rparen()
       && !EOF()
         ) return rejectInner(factor_0);
      return acceptInner(factor_0);
    }
  
  //=====================================================================
  //  rel = op graphclass {rel} ;
  //=====================================================================
  private boolean rel()
    {
      if (saved(rel)) return reuse();
      if (!op()) return reject(rel);
      if (!graphclass()) return reject(rel);
      sem.rel();
      return accept(rel);
    }
  
  //=====================================================================
  //  op = ("<=" / "<" / ">=" / ">" / "=") space ;
  //=====================================================================
  private boolean op()
    {
      if (saved(op)) return reuse();
      if (!next("<=",$Term11)
       && !next('<',$Term12)
       && !next(">=",$Term1)
       && !next('>',$Term19)
       && !next('=',$Term4)
         ) return reject(op);
      space();
      return accept(op);
    }
  
  //=====================================================================
  //  graphclass = (id / name) space {gc} ;
  //=====================================================================
  private boolean graphclass()
    {
      if (saved(graphclass)) return reuse();
      if (!id()
       && !name()
         ) return reject(graphclass);
      space();
      sem.gc();
      return accept(graphclass);
    }
  
  //=====================================================================
  //  name = &["] """ thename (["] / EOL) ;
  //=====================================================================
  private boolean name()
    {
      if (saved(name)) return reuse();
      if (!ahead('"',$Term16)) return reject(name);
      if (!next('"',$Term7)) return reject(name);
      thename();
      if (!next('"',$Term16)
       && !EOL()
         ) return reject(name);
      return accept(name);
    }
  
  //=====================================================================
  //  thename = ^["\r\n]* ;
  //=====================================================================
  private boolean thename()
    {
      if (saved(thename)) return reuse();
      while (nextNotIn("\"\r\n",$Term5));
      return accept(thename);
    }
  
  //=====================================================================
  //  id = !keyword idstart idchar* ;
  //=====================================================================
  private boolean id()
    {
      if (saved(id)) return reuse();
      if (!id_0()) return reject(id);
      if (!idstart()) return reject(id);
      while (idchar());
      return accept(id);
    }
  
  //-------------------------------------------------------------------
  //  id_0 = !keyword
  //-------------------------------------------------------------------
  private boolean id_0()
    {
      if (savedInner(id_0)) return reusePred();
      if (keyword()) return rejectNot(id_0);
      return acceptNot(id_0);
    }
  
  //=====================================================================
  //  idstart = [a-z] / [A-Z] ;
  //=====================================================================
  private boolean idstart()
    {
      if (saved(idstart)) return reuse();
      if (nextIn('a','z',$Term3)) return accept(idstart);
      if (nextIn('A','Z',$Term8)) return accept(idstart);
      return reject(idstart);
    }
  
  //=====================================================================
  //  idchar = idstart / [0-9] / "_" ;
  //=====================================================================
  private boolean idchar()
    {
      if (saved(idchar)) return reuse();
      if (idstart()) return accept(idchar);
      if (nextIn('0','9',$Term14)) return accept(idchar);
      if (next('_',$Term6)) return accept(idchar);
      return reject(idchar);
    }
  
  //=====================================================================
  //  keyword = (and / or / not) ;
  //=====================================================================
  private boolean keyword()
    {
      if (saved(keyword)) return reuse();
      if (!and()
       && !or()
       && !not()
         ) return reject(keyword);
      return accept(keyword);
    }
  
  //=====================================================================
  //  not = "not" !idchar space <'not'> ;
  //=====================================================================
  private boolean not()
    {
      if (saved(not)) return reuse();
      if (!next("not",$Term2)) return reject(not);
      if (!not_0()) return reject(not);
      space();
      return accept(not);
    }
  
  //-------------------------------------------------------------------
  //  not_0 = !idchar
  //-------------------------------------------------------------------
  private boolean not_0()
    {
      if (savedInner(not_0)) return reusePred();
      if (idchar()) return rejectNot(not_0);
      return acceptNot(not_0);
    }
  
  //=====================================================================
  //  or = "or" !idchar space <'or'> ;
  //=====================================================================
  private boolean or()
    {
      if (saved(or)) return reuse();
      if (!next("or",$Term17)) return reject(or);
      if (!not_0()) return reject(or);
      space();
      return accept(or);
    }
  
  //=====================================================================
  //  and = "and" !idchar space <'and'> ;
  //=====================================================================
  private boolean and()
    {
      if (saved(and)) return reuse();
      if (!next("and",$Term18)) return reject(and);
      if (!not_0()) return reject(and);
      space();
      return accept(and);
    }
  
  //=====================================================================
  //  lparen = "(" space ;
  //=====================================================================
  private boolean lparen()
    {
      if (saved(lparen)) return reuse();
      if (!next('(',$Term15)) return reject(lparen);
      space();
      return accept(lparen);
    }
  
  //=====================================================================
  //  rparen = ")" space ;
  //=====================================================================
  private boolean rparen()
    {
      if (saved(rparen)) return reuse();
      if (!next(')',$Term9)) return reject(rparen);
      space();
      return accept(rparen);
    }
  
  //=====================================================================
  //  space = [ \t\r\n]* {space} ;
  //=====================================================================
  private boolean space()
    {
      if (saved(space)) return reuse();
      while (nextIn(" \t\r\n",$Term13));
      sem.space();
      return accept(space);
    }
  
  //=====================================================================
  //  EOL = [\r\n] / EOF {space} ;
  //=====================================================================
  private boolean EOL()
    {
      if (saved(EOL)) return reuse();
      if (nextIn("\r\n",$Term10)) return accept(EOL);
      if (EOF())
      { sem.space(); return accept(EOL); }
      return reject(EOL);
    }
  
  //=====================================================================
  //  EOF = !_ ;
  //=====================================================================
  private boolean EOF()
    {
      if (saved(EOF)) return reuse();
      if (!aheadNot($Term0)) return reject(EOF);
      return accept(EOF);
    }
  
  //=======================================================================
  //
  //  Cache objects
  //
  //=======================================================================
  
  final Cache top = new Cache("top","top");
  final Cache expr = new Cache("expr","expr");
  final Cache term = new Cache("term","term");
  final Cache notfactor = new Cache("notfactor","notfactor");
  final Cache factor = new Cache("factor","factor");
  final Cache rel = new Cache("rel","rel");
  final Cache op = new Cache("op","op");
  final Cache graphclass = new Cache("graphclass","graphclass");
  final Cache name = new Cache("name","name");
  final Cache thename = new Cache("thename","thename");
  final Cache id = new Cache("id","id");
  final Cache idstart = new Cache("idstart","idstart");
  final Cache idchar = new Cache("idchar","idchar");
  final Cache keyword = new Cache("keyword","keyword");
  final Cache not = new Cache("not","'not'");
  final Cache or = new Cache("or","'or'");
  final Cache and = new Cache("and","'and'");
  final Cache lparen = new Cache("lparen","lparen");
  final Cache rparen = new Cache("rparen","rparen");
  final Cache space = new Cache("space","space");
  final Cache EOL = new Cache("EOL","EOL");
  final Cache EOF = new Cache("EOF","EOF");
  
  final Cache top_0 = new Cache("top_0"); // space expr EOF
  final Cache top_1 = new Cache("top_1"); // _++ EOF
  final Cache expr_0 = new Cache("expr_0"); // or term
  final Cache term_0 = new Cache("term_0"); // and notfactor
  final Cache factor_0 = new Cache("factor_0"); // lparen expr (rparen / EOF)
  final Cache id_0 = new Cache("id_0","not keyword"); // !keyword
  final Cache not_0 = new Cache("not_0","not idchar"); // !idchar
  
  final Cache $Term0 = new Cache("_");
  final Cache $Term1 = new Cache("\">=\"");
  final Cache $Term2 = new Cache("\"not\"");
  final Cache $Term3 = new Cache("[a-z]");
  final Cache $Term4 = new Cache("\"=\"");
  final Cache $Term5 = new Cache("^[\"\r\n]");
  final Cache $Term6 = new Cache("\"_\"");
  final Cache $Term7 = new Cache("\"\"\"");
  final Cache $Term8 = new Cache("[A-Z]");
  final Cache $Term9 = new Cache("\")\"");
  final Cache $Term10 = new Cache("[\r\n]");
  final Cache $Term11 = new Cache("\"<=\"");
  final Cache $Term12 = new Cache("\"<\"");
  final Cache $Term13 = new Cache("[ \t\r\n]");
  final Cache $Term14 = new Cache("[0-9]");
  final Cache $Term15 = new Cache("\"(\"");
  final Cache $Term16 = new Cache("[\"]");
  final Cache $Term17 = new Cache("\"or\"");
  final Cache $Term18 = new Cache("\"and\"");
  final Cache $Term19 = new Cache("\">\"");
  
  //-------------------------------------------------------------------
  //  List of Cache objects
  //-------------------------------------------------------------------
  
  Cache[] cacheList =
  {
    top,expr,term,notfactor,factor,rel,op,graphclass,name,thename,id,
    idstart,idchar,keyword,not,or,and,lparen,rparen,space,EOL,EOF,
    top_0,top_1,expr_0,term_0,factor_0,id_0,not_0,$Term0,$Term1,
    $Term2,$Term3,$Term4,$Term5,$Term6,$Term7,$Term8,$Term9,$Term10,
    $Term11,$Term12,$Term13,$Term14,$Term15,$Term16,$Term17,$Term18,
    $Term19
  };
}
