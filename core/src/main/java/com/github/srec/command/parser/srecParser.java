// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/victor/srec/core/src/main/antlr/srec.g 2010-05-05 14:01:43

package com.github.srec.command.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class srecParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "SCRIPT", "REQUIRE", "ASSIGN", "METHOD_CALL_OR_VARREF", "METHOD_CALL", "METHOD_DEF", "METHOD_DEF_PARAMS", "METHOD_BODY", "LITNUMBER", "LITBOOLEAN", "LITSTRING", "LITNIL", "QNAME", "STRING", "NEWLINE", "ID", "NUMBER", "BOOLEAN", "NULL", "DIGIT", "LETTER", "ALPHANUM", "WS", "'require'", "'('", "')'", "','", "'='", "'def'", "'end'"
    };
    public static final int SCRIPT=4;
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int LETTER=24;
    public static final int METHOD_DEF=9;
    public static final int NULL=22;
    public static final int NUMBER=20;
    public static final int ID=19;
    public static final int EOF=-1;
    public static final int REQUIRE=5;
    public static final int METHOD_CALL=8;
    public static final int ALPHANUM=25;
    public static final int METHOD_DEF_PARAMS=10;
    public static final int LITNUMBER=12;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int WS=26;
    public static final int BOOLEAN=21;
    public static final int QNAME=16;
    public static final int T__33=33;
    public static final int NEWLINE=18;
    public static final int METHOD_BODY=11;
    public static final int ASSIGN=6;
    public static final int LITBOOLEAN=13;
    public static final int LITNIL=15;
    public static final int METHOD_CALL_OR_VARREF=7;
    public static final int DIGIT=23;
    public static final int STRING=17;
    public static final int LITSTRING=14;

    // delegates
    // delegators


        public srecParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public srecParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return srecParser.tokenNames; }
    public String getGrammarFileName() { return "/home/victor/srec/core/src/main/antlr/srec.g"; }


    public static class script_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "script"
    // /home/victor/srec/core/src/main/antlr/srec.g:40:1: script : ( require )* ( script_stmt_block )? -> ^( SCRIPT ( require )* ( script_stmt_block )? ) ;
    public final srecParser.script_return script() throws RecognitionException {
        srecParser.script_return retval = new srecParser.script_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        srecParser.require_return require1 = null;

        srecParser.script_stmt_block_return script_stmt_block2 = null;


        RewriteRuleSubtreeStream stream_require=new RewriteRuleSubtreeStream(adaptor,"rule require");
        RewriteRuleSubtreeStream stream_script_stmt_block=new RewriteRuleSubtreeStream(adaptor,"rule script_stmt_block");
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:41:2: ( ( require )* ( script_stmt_block )? -> ^( SCRIPT ( require )* ( script_stmt_block )? ) )
            // /home/victor/srec/core/src/main/antlr/srec.g:41:4: ( require )* ( script_stmt_block )?
            {
            // /home/victor/srec/core/src/main/antlr/srec.g:41:4: ( require )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==27) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:41:4: require
            	    {
            	    pushFollow(FOLLOW_require_in_script117);
            	    require1=require();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_require.add(require1.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // /home/victor/srec/core/src/main/antlr/srec.g:41:13: ( script_stmt_block )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID||LA2_0==32) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:41:13: script_stmt_block
                    {
                    pushFollow(FOLLOW_script_stmt_block_in_script120);
                    script_stmt_block2=script_stmt_block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_script_stmt_block.add(script_stmt_block2.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: require, script_stmt_block
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 42:3: -> ^( SCRIPT ( require )* ( script_stmt_block )? )
            {
                // /home/victor/srec/core/src/main/antlr/srec.g:42:6: ^( SCRIPT ( require )* ( script_stmt_block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SCRIPT, "SCRIPT"), root_1);

                // /home/victor/srec/core/src/main/antlr/srec.g:42:15: ( require )*
                while ( stream_require.hasNext() ) {
                    adaptor.addChild(root_1, stream_require.nextTree());

                }
                stream_require.reset();
                // /home/victor/srec/core/src/main/antlr/srec.g:42:24: ( script_stmt_block )?
                if ( stream_script_stmt_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_script_stmt_block.nextTree());

                }
                stream_script_stmt_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "script"

    public static class require_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "require"
    // /home/victor/srec/core/src/main/antlr/srec.g:45:1: require : 'require' STRING NEWLINE -> ^( REQUIRE STRING ) ;
    public final srecParser.require_return require() throws RecognitionException {
        srecParser.require_return retval = new srecParser.require_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal3=null;
        Token STRING4=null;
        Token NEWLINE5=null;

        CommonTree string_literal3_tree=null;
        CommonTree STRING4_tree=null;
        CommonTree NEWLINE5_tree=null;
        RewriteRuleTokenStream stream_NEWLINE=new RewriteRuleTokenStream(adaptor,"token NEWLINE");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:46:2: ( 'require' STRING NEWLINE -> ^( REQUIRE STRING ) )
            // /home/victor/srec/core/src/main/antlr/srec.g:46:4: 'require' STRING NEWLINE
            {
            string_literal3=(Token)match(input,27,FOLLOW_27_in_require151); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_27.add(string_literal3);

            STRING4=(Token)match(input,STRING,FOLLOW_STRING_in_require153); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_STRING.add(STRING4);

            NEWLINE5=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_require155); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_NEWLINE.add(NEWLINE5);



            // AST REWRITE
            // elements: STRING
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 47:3: -> ^( REQUIRE STRING )
            {
                // /home/victor/srec/core/src/main/antlr/srec.g:47:6: ^( REQUIRE STRING )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(REQUIRE, "REQUIRE"), root_1);

                adaptor.addChild(root_1, stream_STRING.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "require"

    public static class script_stmt_block_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "script_stmt_block"
    // /home/victor/srec/core/src/main/antlr/srec.g:50:1: script_stmt_block : ( expression ( NEWLINE | EOF ) )+ ;
    public final srecParser.script_stmt_block_return script_stmt_block() throws RecognitionException {
        srecParser.script_stmt_block_return retval = new srecParser.script_stmt_block_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NEWLINE7=null;
        Token EOF8=null;
        srecParser.expression_return expression6 = null;


        CommonTree NEWLINE7_tree=null;
        CommonTree EOF8_tree=null;

        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:51:2: ( ( expression ( NEWLINE | EOF ) )+ )
            // /home/victor/srec/core/src/main/antlr/srec.g:51:4: ( expression ( NEWLINE | EOF ) )+
            {
            root_0 = (CommonTree)adaptor.nil();

            // /home/victor/srec/core/src/main/antlr/srec.g:51:4: ( expression ( NEWLINE | EOF ) )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==ID||LA4_0==32) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:51:5: expression ( NEWLINE | EOF )
            	    {
            	    pushFollow(FOLLOW_expression_in_script_stmt_block179);
            	    expression6=expression();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, expression6.getTree());
            	    // /home/victor/srec/core/src/main/antlr/srec.g:51:16: ( NEWLINE | EOF )
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==NEWLINE) ) {
            	        alt3=1;
            	    }
            	    else if ( (LA3_0==EOF) ) {
            	        alt3=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 3, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // /home/victor/srec/core/src/main/antlr/srec.g:51:17: NEWLINE
            	            {
            	            NEWLINE7=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_script_stmt_block182); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            NEWLINE7_tree = (CommonTree)adaptor.create(NEWLINE7);
            	            adaptor.addChild(root_0, NEWLINE7_tree);
            	            }

            	            }
            	            break;
            	        case 2 :
            	            // /home/victor/srec/core/src/main/antlr/srec.g:51:27: EOF
            	            {
            	            EOF8=(Token)match(input,EOF,FOLLOW_EOF_in_script_stmt_block186); if (state.failed) return retval;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "script_stmt_block"

    public static class expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // /home/victor/srec/core/src/main/antlr/srec.g:54:1: expression : ( method_call_or_varref | assignment | method_def );
    public final srecParser.expression_return expression() throws RecognitionException {
        srecParser.expression_return retval = new srecParser.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        srecParser.method_call_or_varref_return method_call_or_varref9 = null;

        srecParser.assignment_return assignment10 = null;

        srecParser.method_def_return method_def11 = null;



        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:55:2: ( method_call_or_varref | assignment | method_def )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==EOF||(LA5_1>=STRING && LA5_1<=NULL)||LA5_1==28) ) {
                    alt5=1;
                }
                else if ( (LA5_1==31) ) {
                    alt5=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA5_0==32) ) {
                alt5=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:55:4: method_call_or_varref
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_method_call_or_varref_in_expression202);
                    method_call_or_varref9=method_call_or_varref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, method_call_or_varref9.getTree());

                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:55:28: assignment
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_assignment_in_expression206);
                    assignment10=assignment();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, assignment10.getTree());

                    }
                    break;
                case 3 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:55:41: method_def
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_method_def_in_expression210);
                    method_def11=method_def();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, method_def11.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class method_call_or_varref_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_call_or_varref"
    // /home/victor/srec/core/src/main/antlr/srec.g:58:1: method_call_or_varref options {backtrack=true; } : ( ID -> ^( METHOD_CALL_OR_VARREF ID ) | ID '(' ')' -> ^( METHOD_CALL ID ) | ID '(' method_call_param ( ',' method_call_param )* ')' -> ^( METHOD_CALL ID ( method_call_param )+ ) | ID method_call_param ( ',' method_call_param )* -> ^( METHOD_CALL ID ( method_call_param )+ ) );
    public final srecParser.method_call_or_varref_return method_call_or_varref() throws RecognitionException {
        srecParser.method_call_or_varref_return retval = new srecParser.method_call_or_varref_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID12=null;
        Token ID13=null;
        Token char_literal14=null;
        Token char_literal15=null;
        Token ID16=null;
        Token char_literal17=null;
        Token char_literal19=null;
        Token char_literal21=null;
        Token ID22=null;
        Token char_literal24=null;
        srecParser.method_call_param_return method_call_param18 = null;

        srecParser.method_call_param_return method_call_param20 = null;

        srecParser.method_call_param_return method_call_param23 = null;

        srecParser.method_call_param_return method_call_param25 = null;


        CommonTree ID12_tree=null;
        CommonTree ID13_tree=null;
        CommonTree char_literal14_tree=null;
        CommonTree char_literal15_tree=null;
        CommonTree ID16_tree=null;
        CommonTree char_literal17_tree=null;
        CommonTree char_literal19_tree=null;
        CommonTree char_literal21_tree=null;
        CommonTree ID22_tree=null;
        CommonTree char_literal24_tree=null;
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
        RewriteRuleSubtreeStream stream_method_call_param=new RewriteRuleSubtreeStream(adaptor,"rule method_call_param");
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:60:2: ( ID -> ^( METHOD_CALL_OR_VARREF ID ) | ID '(' ')' -> ^( METHOD_CALL ID ) | ID '(' method_call_param ( ',' method_call_param )* ')' -> ^( METHOD_CALL ID ( method_call_param )+ ) | ID method_call_param ( ',' method_call_param )* -> ^( METHOD_CALL ID ( method_call_param )+ ) )
            int alt8=4;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ID) ) {
                switch ( input.LA(2) ) {
                case 28:
                    {
                    int LA8_2 = input.LA(3);

                    if ( (LA8_2==29) ) {
                        alt8=2;
                    }
                    else if ( (LA8_2==STRING||(LA8_2>=ID && LA8_2<=NULL)) ) {
                        alt8=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 2, input);

                        throw nvae;
                    }
                    }
                    break;
                case STRING:
                case ID:
                case NUMBER:
                case BOOLEAN:
                case NULL:
                    {
                    alt8=4;
                    }
                    break;
                case EOF:
                case NEWLINE:
                case 29:
                case 30:
                    {
                    alt8=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;
                }

            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:60:4: ID
                    {
                    ID12=(Token)match(input,ID,FOLLOW_ID_in_method_call_or_varref229); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID12);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 61:3: -> ^( METHOD_CALL_OR_VARREF ID )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:61:6: ^( METHOD_CALL_OR_VARREF ID )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_CALL_OR_VARREF, "METHOD_CALL_OR_VARREF"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:62:4: ID '(' ')'
                    {
                    ID13=(Token)match(input,ID,FOLLOW_ID_in_method_call_or_varref244); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID13);

                    char_literal14=(Token)match(input,28,FOLLOW_28_in_method_call_or_varref246); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_28.add(char_literal14);

                    char_literal15=(Token)match(input,29,FOLLOW_29_in_method_call_or_varref248); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_29.add(char_literal15);



                    // AST REWRITE
                    // elements: ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 63:3: -> ^( METHOD_CALL ID )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:63:6: ^( METHOD_CALL ID )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_CALL, "METHOD_CALL"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:64:4: ID '(' method_call_param ( ',' method_call_param )* ')'
                    {
                    ID16=(Token)match(input,ID,FOLLOW_ID_in_method_call_or_varref263); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID16);

                    char_literal17=(Token)match(input,28,FOLLOW_28_in_method_call_or_varref265); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_28.add(char_literal17);

                    pushFollow(FOLLOW_method_call_param_in_method_call_or_varref267);
                    method_call_param18=method_call_param();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_call_param.add(method_call_param18.getTree());
                    // /home/victor/srec/core/src/main/antlr/srec.g:64:29: ( ',' method_call_param )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==30) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /home/victor/srec/core/src/main/antlr/srec.g:64:30: ',' method_call_param
                    	    {
                    	    char_literal19=(Token)match(input,30,FOLLOW_30_in_method_call_or_varref270); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_30.add(char_literal19);

                    	    pushFollow(FOLLOW_method_call_param_in_method_call_or_varref272);
                    	    method_call_param20=method_call_param();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_method_call_param.add(method_call_param20.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    char_literal21=(Token)match(input,29,FOLLOW_29_in_method_call_or_varref276); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_29.add(char_literal21);



                    // AST REWRITE
                    // elements: method_call_param, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 65:3: -> ^( METHOD_CALL ID ( method_call_param )+ )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:65:6: ^( METHOD_CALL ID ( method_call_param )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_CALL, "METHOD_CALL"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        if ( !(stream_method_call_param.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_method_call_param.hasNext() ) {
                            adaptor.addChild(root_1, stream_method_call_param.nextTree());

                        }
                        stream_method_call_param.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:66:4: ID method_call_param ( ',' method_call_param )*
                    {
                    ID22=(Token)match(input,ID,FOLLOW_ID_in_method_call_or_varref294); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID22);

                    pushFollow(FOLLOW_method_call_param_in_method_call_or_varref296);
                    method_call_param23=method_call_param();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_call_param.add(method_call_param23.getTree());
                    // /home/victor/srec/core/src/main/antlr/srec.g:66:25: ( ',' method_call_param )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==30) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /home/victor/srec/core/src/main/antlr/srec.g:66:26: ',' method_call_param
                    	    {
                    	    char_literal24=(Token)match(input,30,FOLLOW_30_in_method_call_or_varref299); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_30.add(char_literal24);

                    	    pushFollow(FOLLOW_method_call_param_in_method_call_or_varref301);
                    	    method_call_param25=method_call_param();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_method_call_param.add(method_call_param25.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);



                    // AST REWRITE
                    // elements: ID, method_call_param
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 67:3: -> ^( METHOD_CALL ID ( method_call_param )+ )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:67:6: ^( METHOD_CALL ID ( method_call_param )+ )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_CALL, "METHOD_CALL"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        if ( !(stream_method_call_param.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_method_call_param.hasNext() ) {
                            adaptor.addChild(root_1, stream_method_call_param.nextTree());

                        }
                        stream_method_call_param.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_call_or_varref"

    public static class method_call_param_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_call_param"
    // /home/victor/srec/core/src/main/antlr/srec.g:70:1: method_call_param : ( literal | method_call_or_varref );
    public final srecParser.method_call_param_return method_call_param() throws RecognitionException {
        srecParser.method_call_param_return retval = new srecParser.method_call_param_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        srecParser.literal_return literal26 = null;

        srecParser.method_call_or_varref_return method_call_or_varref27 = null;



        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:71:2: ( literal | method_call_or_varref )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==STRING||(LA9_0>=NUMBER && LA9_0<=NULL)) ) {
                alt9=1;
            }
            else if ( (LA9_0==ID) ) {
                alt9=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:71:4: literal
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_method_call_param327);
                    literal26=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal26.getTree());

                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:71:14: method_call_or_varref
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_method_call_or_varref_in_method_call_param331);
                    method_call_or_varref27=method_call_or_varref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, method_call_or_varref27.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_call_param"

    public static class assignment_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "assignment"
    // /home/victor/srec/core/src/main/antlr/srec.g:74:1: assignment : ID '=' ( literal | method_call_or_varref ) -> ^( ASSIGN ID ( literal )? ( method_call_or_varref )? ) ;
    public final srecParser.assignment_return assignment() throws RecognitionException {
        srecParser.assignment_return retval = new srecParser.assignment_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID28=null;
        Token char_literal29=null;
        srecParser.literal_return literal30 = null;

        srecParser.method_call_or_varref_return method_call_or_varref31 = null;


        CommonTree ID28_tree=null;
        CommonTree char_literal29_tree=null;
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_method_call_or_varref=new RewriteRuleSubtreeStream(adaptor,"rule method_call_or_varref");
        RewriteRuleSubtreeStream stream_literal=new RewriteRuleSubtreeStream(adaptor,"rule literal");
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:75:2: ( ID '=' ( literal | method_call_or_varref ) -> ^( ASSIGN ID ( literal )? ( method_call_or_varref )? ) )
            // /home/victor/srec/core/src/main/antlr/srec.g:75:4: ID '=' ( literal | method_call_or_varref )
            {
            ID28=(Token)match(input,ID,FOLLOW_ID_in_assignment343); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID28);

            char_literal29=(Token)match(input,31,FOLLOW_31_in_assignment345); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_31.add(char_literal29);

            // /home/victor/srec/core/src/main/antlr/srec.g:75:11: ( literal | method_call_or_varref )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==STRING||(LA10_0>=NUMBER && LA10_0<=NULL)) ) {
                alt10=1;
            }
            else if ( (LA10_0==ID) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:75:12: literal
                    {
                    pushFollow(FOLLOW_literal_in_assignment348);
                    literal30=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_literal.add(literal30.getTree());

                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:75:22: method_call_or_varref
                    {
                    pushFollow(FOLLOW_method_call_or_varref_in_assignment352);
                    method_call_or_varref31=method_call_or_varref();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_call_or_varref.add(method_call_or_varref31.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: method_call_or_varref, literal, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 76:3: -> ^( ASSIGN ID ( literal )? ( method_call_or_varref )? )
            {
                // /home/victor/srec/core/src/main/antlr/srec.g:76:6: ^( ASSIGN ID ( literal )? ( method_call_or_varref )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ASSIGN, "ASSIGN"), root_1);

                adaptor.addChild(root_1, stream_ID.nextNode());
                // /home/victor/srec/core/src/main/antlr/srec.g:76:18: ( literal )?
                if ( stream_literal.hasNext() ) {
                    adaptor.addChild(root_1, stream_literal.nextTree());

                }
                stream_literal.reset();
                // /home/victor/srec/core/src/main/antlr/srec.g:76:27: ( method_call_or_varref )?
                if ( stream_method_call_or_varref.hasNext() ) {
                    adaptor.addChild(root_1, stream_method_call_or_varref.nextTree());

                }
                stream_method_call_or_varref.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "assignment"

    public static class method_def_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_def"
    // /home/victor/srec/core/src/main/antlr/srec.g:79:1: method_def : ( 'def' ID NEWLINE method_body 'end' -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body ) | 'def' ID '(' ')' NEWLINE method_body 'end' -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body ) | 'def' ID '(' method_def_params ')' NEWLINE method_body 'end' -> ^( METHOD_DEF ID method_def_params method_body ) );
    public final srecParser.method_def_return method_def() throws RecognitionException {
        srecParser.method_def_return retval = new srecParser.method_def_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal32=null;
        Token ID33=null;
        Token NEWLINE34=null;
        Token string_literal36=null;
        Token string_literal37=null;
        Token ID38=null;
        Token char_literal39=null;
        Token char_literal40=null;
        Token NEWLINE41=null;
        Token string_literal43=null;
        Token string_literal44=null;
        Token ID45=null;
        Token char_literal46=null;
        Token char_literal48=null;
        Token NEWLINE49=null;
        Token string_literal51=null;
        srecParser.method_body_return method_body35 = null;

        srecParser.method_body_return method_body42 = null;

        srecParser.method_def_params_return method_def_params47 = null;

        srecParser.method_body_return method_body50 = null;


        CommonTree string_literal32_tree=null;
        CommonTree ID33_tree=null;
        CommonTree NEWLINE34_tree=null;
        CommonTree string_literal36_tree=null;
        CommonTree string_literal37_tree=null;
        CommonTree ID38_tree=null;
        CommonTree char_literal39_tree=null;
        CommonTree char_literal40_tree=null;
        CommonTree NEWLINE41_tree=null;
        CommonTree string_literal43_tree=null;
        CommonTree string_literal44_tree=null;
        CommonTree ID45_tree=null;
        CommonTree char_literal46_tree=null;
        CommonTree char_literal48_tree=null;
        CommonTree NEWLINE49_tree=null;
        CommonTree string_literal51_tree=null;
        RewriteRuleTokenStream stream_32=new RewriteRuleTokenStream(adaptor,"token 32");
        RewriteRuleTokenStream stream_NEWLINE=new RewriteRuleTokenStream(adaptor,"token NEWLINE");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleTokenStream stream_29=new RewriteRuleTokenStream(adaptor,"token 29");
        RewriteRuleSubtreeStream stream_method_def_params=new RewriteRuleSubtreeStream(adaptor,"rule method_def_params");
        RewriteRuleSubtreeStream stream_method_body=new RewriteRuleSubtreeStream(adaptor,"rule method_body");
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:80:2: ( 'def' ID NEWLINE method_body 'end' -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body ) | 'def' ID '(' ')' NEWLINE method_body 'end' -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body ) | 'def' ID '(' method_def_params ')' NEWLINE method_body 'end' -> ^( METHOD_DEF ID method_def_params method_body ) )
            int alt11=3;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==32) ) {
                int LA11_1 = input.LA(2);

                if ( (LA11_1==ID) ) {
                    int LA11_2 = input.LA(3);

                    if ( (LA11_2==NEWLINE) ) {
                        alt11=1;
                    }
                    else if ( (LA11_2==28) ) {
                        int LA11_4 = input.LA(4);

                        if ( (LA11_4==29) ) {
                            alt11=2;
                        }
                        else if ( (LA11_4==ID) ) {
                            alt11=3;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 11, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 11, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:80:4: 'def' ID NEWLINE method_body 'end'
                    {
                    string_literal32=(Token)match(input,32,FOLLOW_32_in_method_def380); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_32.add(string_literal32);

                    ID33=(Token)match(input,ID,FOLLOW_ID_in_method_def382); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID33);

                    NEWLINE34=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_method_def384); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEWLINE.add(NEWLINE34);

                    pushFollow(FOLLOW_method_body_in_method_def386);
                    method_body35=method_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_body.add(method_body35.getTree());
                    string_literal36=(Token)match(input,33,FOLLOW_33_in_method_def388); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_33.add(string_literal36);



                    // AST REWRITE
                    // elements: ID, method_body
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 81:3: -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:81:6: ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF, "METHOD_DEF"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /home/victor/srec/core/src/main/antlr/srec.g:81:22: ^( METHOD_DEF_PARAMS )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF_PARAMS, "METHOD_DEF_PARAMS"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_method_body.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:82:4: 'def' ID '(' ')' NEWLINE method_body 'end'
                    {
                    string_literal37=(Token)match(input,32,FOLLOW_32_in_method_def409); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_32.add(string_literal37);

                    ID38=(Token)match(input,ID,FOLLOW_ID_in_method_def411); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID38);

                    char_literal39=(Token)match(input,28,FOLLOW_28_in_method_def413); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_28.add(char_literal39);

                    char_literal40=(Token)match(input,29,FOLLOW_29_in_method_def415); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_29.add(char_literal40);

                    NEWLINE41=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_method_def417); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEWLINE.add(NEWLINE41);

                    pushFollow(FOLLOW_method_body_in_method_def419);
                    method_body42=method_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_body.add(method_body42.getTree());
                    string_literal43=(Token)match(input,33,FOLLOW_33_in_method_def421); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_33.add(string_literal43);



                    // AST REWRITE
                    // elements: ID, method_body
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 83:3: -> ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:83:6: ^( METHOD_DEF ID ^( METHOD_DEF_PARAMS ) method_body )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF, "METHOD_DEF"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        // /home/victor/srec/core/src/main/antlr/srec.g:83:22: ^( METHOD_DEF_PARAMS )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF_PARAMS, "METHOD_DEF_PARAMS"), root_2);

                        adaptor.addChild(root_1, root_2);
                        }
                        adaptor.addChild(root_1, stream_method_body.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:84:4: 'def' ID '(' method_def_params ')' NEWLINE method_body 'end'
                    {
                    string_literal44=(Token)match(input,32,FOLLOW_32_in_method_def442); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_32.add(string_literal44);

                    ID45=(Token)match(input,ID,FOLLOW_ID_in_method_def444); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(ID45);

                    char_literal46=(Token)match(input,28,FOLLOW_28_in_method_def446); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_28.add(char_literal46);

                    pushFollow(FOLLOW_method_def_params_in_method_def448);
                    method_def_params47=method_def_params();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_def_params.add(method_def_params47.getTree());
                    char_literal48=(Token)match(input,29,FOLLOW_29_in_method_def450); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_29.add(char_literal48);

                    NEWLINE49=(Token)match(input,NEWLINE,FOLLOW_NEWLINE_in_method_def452); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NEWLINE.add(NEWLINE49);

                    pushFollow(FOLLOW_method_body_in_method_def454);
                    method_body50=method_body();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_method_body.add(method_body50.getTree());
                    string_literal51=(Token)match(input,33,FOLLOW_33_in_method_def456); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_33.add(string_literal51);



                    // AST REWRITE
                    // elements: method_body, ID, method_def_params
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 85:3: -> ^( METHOD_DEF ID method_def_params method_body )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:85:6: ^( METHOD_DEF ID method_def_params method_body )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF, "METHOD_DEF"), root_1);

                        adaptor.addChild(root_1, stream_ID.nextNode());
                        adaptor.addChild(root_1, stream_method_def_params.nextTree());
                        adaptor.addChild(root_1, stream_method_body.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_def"

    public static class method_body_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_body"
    // /home/victor/srec/core/src/main/antlr/srec.g:88:1: method_body : ( script_stmt_block )? -> ^( METHOD_BODY ( script_stmt_block )? ) ;
    public final srecParser.method_body_return method_body() throws RecognitionException {
        srecParser.method_body_return retval = new srecParser.method_body_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        srecParser.script_stmt_block_return script_stmt_block52 = null;


        RewriteRuleSubtreeStream stream_script_stmt_block=new RewriteRuleSubtreeStream(adaptor,"rule script_stmt_block");
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:89:2: ( ( script_stmt_block )? -> ^( METHOD_BODY ( script_stmt_block )? ) )
            // /home/victor/srec/core/src/main/antlr/srec.g:89:4: ( script_stmt_block )?
            {
            // /home/victor/srec/core/src/main/antlr/srec.g:89:4: ( script_stmt_block )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID||LA12_0==32) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:89:4: script_stmt_block
                    {
                    pushFollow(FOLLOW_script_stmt_block_in_method_body481);
                    script_stmt_block52=script_stmt_block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_script_stmt_block.add(script_stmt_block52.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: script_stmt_block
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 90:3: -> ^( METHOD_BODY ( script_stmt_block )? )
            {
                // /home/victor/srec/core/src/main/antlr/srec.g:90:6: ^( METHOD_BODY ( script_stmt_block )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_BODY, "METHOD_BODY"), root_1);

                // /home/victor/srec/core/src/main/antlr/srec.g:90:20: ( script_stmt_block )?
                if ( stream_script_stmt_block.hasNext() ) {
                    adaptor.addChild(root_1, stream_script_stmt_block.nextTree());

                }
                stream_script_stmt_block.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_body"

    public static class method_def_params_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_def_params"
    // /home/victor/srec/core/src/main/antlr/srec.g:93:1: method_def_params : ID ( ',' ID )* -> ^( METHOD_DEF_PARAMS ( ID )+ ) ;
    public final srecParser.method_def_params_return method_def_params() throws RecognitionException {
        srecParser.method_def_params_return retval = new srecParser.method_def_params_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID53=null;
        Token char_literal54=null;
        Token ID55=null;

        CommonTree ID53_tree=null;
        CommonTree char_literal54_tree=null;
        CommonTree ID55_tree=null;
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:94:2: ( ID ( ',' ID )* -> ^( METHOD_DEF_PARAMS ( ID )+ ) )
            // /home/victor/srec/core/src/main/antlr/srec.g:94:4: ID ( ',' ID )*
            {
            ID53=(Token)match(input,ID,FOLLOW_ID_in_method_def_params504); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(ID53);

            // /home/victor/srec/core/src/main/antlr/srec.g:94:7: ( ',' ID )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==30) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:94:8: ',' ID
            	    {
            	    char_literal54=(Token)match(input,30,FOLLOW_30_in_method_def_params507); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_30.add(char_literal54);

            	    ID55=(Token)match(input,ID,FOLLOW_ID_in_method_def_params509); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_ID.add(ID55);


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);



            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 95:3: -> ^( METHOD_DEF_PARAMS ( ID )+ )
            {
                // /home/victor/srec/core/src/main/antlr/srec.g:95:6: ^( METHOD_DEF_PARAMS ( ID )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(METHOD_DEF_PARAMS, "METHOD_DEF_PARAMS"), root_1);

                if ( !(stream_ID.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_ID.hasNext() ) {
                    adaptor.addChild(root_1, stream_ID.nextNode());

                }
                stream_ID.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_def_params"

    public static class literal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // /home/victor/srec/core/src/main/antlr/srec.g:98:1: literal : ( NUMBER -> ^( LITNUMBER NUMBER ) | BOOLEAN -> ^( LITBOOLEAN BOOLEAN ) | STRING -> ^( LITSTRING STRING ) | NULL -> LITNIL );
    public final srecParser.literal_return literal() throws RecognitionException {
        srecParser.literal_return retval = new srecParser.literal_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token NUMBER56=null;
        Token BOOLEAN57=null;
        Token STRING58=null;
        Token NULL59=null;

        CommonTree NUMBER56_tree=null;
        CommonTree BOOLEAN57_tree=null;
        CommonTree STRING58_tree=null;
        CommonTree NULL59_tree=null;
        RewriteRuleTokenStream stream_BOOLEAN=new RewriteRuleTokenStream(adaptor,"token BOOLEAN");
        RewriteRuleTokenStream stream_NULL=new RewriteRuleTokenStream(adaptor,"token NULL");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");

        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:99:2: ( NUMBER -> ^( LITNUMBER NUMBER ) | BOOLEAN -> ^( LITBOOLEAN BOOLEAN ) | STRING -> ^( LITSTRING STRING ) | NULL -> LITNIL )
            int alt14=4;
            switch ( input.LA(1) ) {
            case NUMBER:
                {
                alt14=1;
                }
                break;
            case BOOLEAN:
                {
                alt14=2;
                }
                break;
            case STRING:
                {
                alt14=3;
                }
                break;
            case NULL:
                {
                alt14=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }

            switch (alt14) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:99:4: NUMBER
                    {
                    NUMBER56=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_literal535); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NUMBER.add(NUMBER56);



                    // AST REWRITE
                    // elements: NUMBER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 100:3: -> ^( LITNUMBER NUMBER )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:100:6: ^( LITNUMBER NUMBER )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LITNUMBER, "LITNUMBER"), root_1);

                        adaptor.addChild(root_1, stream_NUMBER.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:102:4: BOOLEAN
                    {
                    BOOLEAN57=(Token)match(input,BOOLEAN,FOLLOW_BOOLEAN_in_literal554); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_BOOLEAN.add(BOOLEAN57);



                    // AST REWRITE
                    // elements: BOOLEAN
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 103:3: -> ^( LITBOOLEAN BOOLEAN )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:103:6: ^( LITBOOLEAN BOOLEAN )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LITBOOLEAN, "LITBOOLEAN"), root_1);

                        adaptor.addChild(root_1, stream_BOOLEAN.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:105:4: STRING
                    {
                    STRING58=(Token)match(input,STRING,FOLLOW_STRING_in_literal572); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_STRING.add(STRING58);



                    // AST REWRITE
                    // elements: STRING
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 106:3: -> ^( LITSTRING STRING )
                    {
                        // /home/victor/srec/core/src/main/antlr/srec.g:106:6: ^( LITSTRING STRING )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LITSTRING, "LITSTRING"), root_1);

                        adaptor.addChild(root_1, stream_STRING.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:108:4: NULL
                    {
                    NULL59=(Token)match(input,NULL,FOLLOW_NULL_in_literal591); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_NULL.add(NULL59);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 109:3: -> LITNIL
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(LITNIL, "LITNIL"));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    // Delegated rules


 

    public static final BitSet FOLLOW_require_in_script117 = new BitSet(new long[]{0x0000000108080002L});
    public static final BitSet FOLLOW_script_stmt_block_in_script120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_require151 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_STRING_in_require153 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NEWLINE_in_require155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_script_stmt_block179 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NEWLINE_in_script_stmt_block182 = new BitSet(new long[]{0x0000000100080002L});
    public static final BitSet FOLLOW_EOF_in_script_stmt_block186 = new BitSet(new long[]{0x0000000100080002L});
    public static final BitSet FOLLOW_method_call_or_varref_in_expression202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignment_in_expression206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_method_def_in_expression210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_call_or_varref229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_call_or_varref244 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_method_call_or_varref246 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_method_call_or_varref248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_call_or_varref263 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_method_call_or_varref265 = new BitSet(new long[]{0x00000000007A0000L});
    public static final BitSet FOLLOW_method_call_param_in_method_call_or_varref267 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_30_in_method_call_or_varref270 = new BitSet(new long[]{0x00000000007A0000L});
    public static final BitSet FOLLOW_method_call_param_in_method_call_or_varref272 = new BitSet(new long[]{0x0000000060000000L});
    public static final BitSet FOLLOW_29_in_method_call_or_varref276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_call_or_varref294 = new BitSet(new long[]{0x00000000007A0000L});
    public static final BitSet FOLLOW_method_call_param_in_method_call_or_varref296 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_method_call_or_varref299 = new BitSet(new long[]{0x00000000007A0000L});
    public static final BitSet FOLLOW_method_call_param_in_method_call_or_varref301 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_literal_in_method_call_param327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_method_call_or_varref_in_method_call_param331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_assignment343 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_assignment345 = new BitSet(new long[]{0x00000000007A0000L});
    public static final BitSet FOLLOW_literal_in_assignment348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_method_call_or_varref_in_assignment352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_method_def380 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_ID_in_method_def382 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NEWLINE_in_method_def384 = new BitSet(new long[]{0x0000000300080000L});
    public static final BitSet FOLLOW_method_body_in_method_def386 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_method_def388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_method_def409 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_ID_in_method_def411 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_method_def413 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_method_def415 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NEWLINE_in_method_def417 = new BitSet(new long[]{0x0000000300080000L});
    public static final BitSet FOLLOW_method_body_in_method_def419 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_method_def421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_method_def442 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_ID_in_method_def444 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_method_def446 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_method_def_params_in_method_def448 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_method_def450 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_NEWLINE_in_method_def452 = new BitSet(new long[]{0x0000000300080000L});
    public static final BitSet FOLLOW_method_body_in_method_def454 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_method_def456 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_script_stmt_block_in_method_body481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_def_params504 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_method_def_params507 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_ID_in_method_def_params509 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_NUMBER_in_literal535 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOLEAN_in_literal554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_literal591 = new BitSet(new long[]{0x0000000000000002L});

}