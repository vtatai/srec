// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/victor/srec/core/src/main/antlr/srec.g 2010-05-13 17:06:38

package com.github.srec.command.parser.antlr;


import org.antlr.runtime.*;

public class srecLexer extends Lexer {
    public static final int SCRIPT=4;
    public static final int T__29=29;
    public static final int T__28=28;
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
    public static final int T__33=33;
    public static final int QNAME=16;
    public static final int BOOLEAN=21;
    public static final int WS=26;
    public static final int T__34=34;
    public static final int NEWLINE=18;
    public static final int METHOD_BODY=11;
    public static final int ASSIGN=6;
    public static final int LITBOOLEAN=13;
    public static final int LITNIL=15;
    public static final int METHOD_CALL_OR_VARREF=7;
    public static final int DIGIT=23;
    public static final int COMMENT=27;
    public static final int STRING=17;
    public static final int LITSTRING=14;

    // delegates
    // delegators

    public srecLexer() {;} 
    public srecLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public srecLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/victor/srec/core/src/main/antlr/srec.g"; }

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:7:7: ( 'require' )
            // /home/victor/srec/core/src/main/antlr/srec.g:7:9: 'require'
            {
            match("require"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:8:7: ( '(' )
            // /home/victor/srec/core/src/main/antlr/srec.g:8:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:9:7: ( ')' )
            // /home/victor/srec/core/src/main/antlr/srec.g:9:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:10:7: ( ',' )
            // /home/victor/srec/core/src/main/antlr/srec.g:10:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:11:7: ( '=' )
            // /home/victor/srec/core/src/main/antlr/srec.g:11:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:12:7: ( 'def' )
            // /home/victor/srec/core/src/main/antlr/srec.g:12:9: 'def'
            {
            match("def"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:13:7: ( 'end' )
            // /home/victor/srec/core/src/main/antlr/srec.g:13:9: 'end'
            {
            match("end"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "BOOLEAN"
    public final void mBOOLEAN() throws RecognitionException {
        try {
            int _type = BOOLEAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:114:2: ( 'true' | 'false' )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='t') ) {
                alt1=1;
            }
            else if ( (LA1_0=='f') ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:114:4: 'true'
                    {
                    match("true"); 


                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:114:13: 'false'
                    {
                    match("false"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BOOLEAN"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:118:2: ( 'nil' )
            // /home/victor/srec/core/src/main/antlr/srec.g:118:4: 'nil'
            {
            match("nil"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:122:2: ( '0' .. '9' )
            // /home/victor/srec/core/src/main/antlr/srec.g:122:4: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:126:2: ( 'a' .. 'z' | 'A' .. 'Z' )
            // /home/victor/srec/core/src/main/antlr/srec.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "ALPHANUM"
    public final void mALPHANUM() throws RecognitionException {
        try {
            // /home/victor/srec/core/src/main/antlr/srec.g:130:2: ( DIGIT | LETTER )
            // /home/victor/srec/core/src/main/antlr/srec.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ALPHANUM"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:134:2: ( ( LETTER | '_' ) ( ALPHANUM | '_' )* )
            // /home/victor/srec/core/src/main/antlr/srec.g:134:4: ( LETTER | '_' ) ( ALPHANUM | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /home/victor/srec/core/src/main/antlr/srec.g:134:19: ( ALPHANUM | '_' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:138:2: ( ( DIGIT )+ )
            // /home/victor/srec/core/src/main/antlr/srec.g:138:4: ( DIGIT )+
            {
            // /home/victor/srec/core/src/main/antlr/srec.g:138:4: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:138:4: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:142:2: ( '\"' ( . )* '\"' | '\\'' ( . )* '\\'' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\"') ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:142:4: '\"' ( . )* '\"'
                    {
                    match('\"'); 
                    // /home/victor/srec/core/src/main/antlr/srec.g:142:8: ( . )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0=='\"') ) {
                            alt4=2;
                        }
                        else if ( ((LA4_0>='\u0000' && LA4_0<='!')||(LA4_0>='#' && LA4_0<='\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /home/victor/srec/core/src/main/antlr/srec.g:142:8: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);

                    match('\"'); 
                    setText(getText().substring(1, getText().length()-1));

                    }
                    break;
                case 2 :
                    // /home/victor/srec/core/src/main/antlr/srec.g:143:4: '\\'' ( . )* '\\''
                    {
                    match('\''); 
                    // /home/victor/srec/core/src/main/antlr/srec.g:143:9: ( . )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\'') ) {
                            alt5=2;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='&')||(LA5_0>='(' && LA5_0<='\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/victor/srec/core/src/main/antlr/srec.g:143:9: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match('\''); 
                    setText(getText().substring(1, getText().length()-1));

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:147:2: ( ( ' ' | '\\t' )+ )
            // /home/victor/srec/core/src/main/antlr/srec.g:147:4: ( ' ' | '\\t' )+
            {
            // /home/victor/srec/core/src/main/antlr/srec.g:147:4: ( ' ' | '\\t' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\t'||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:151:2: ( ( ( '\\r' )? '\\n' ( ' ' )* )+ )
            // /home/victor/srec/core/src/main/antlr/srec.g:151:4: ( ( '\\r' )? '\\n' ( ' ' )* )+
            {
            // /home/victor/srec/core/src/main/antlr/srec.g:151:4: ( ( '\\r' )? '\\n' ( ' ' )* )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\n'||LA10_0=='\r') ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:151:5: ( '\\r' )? '\\n' ( ' ' )*
            	    {
            	    // /home/victor/srec/core/src/main/antlr/srec.g:151:5: ( '\\r' )?
            	    int alt8=2;
            	    int LA8_0 = input.LA(1);

            	    if ( (LA8_0=='\r') ) {
            	        alt8=1;
            	    }
            	    switch (alt8) {
            	        case 1 :
            	            // /home/victor/srec/core/src/main/antlr/srec.g:151:5: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 
            	    // /home/victor/srec/core/src/main/antlr/srec.g:151:16: ( ' ' )*
            	    loop9:
            	    do {
            	        int alt9=2;
            	        int LA9_0 = input.LA(1);

            	        if ( (LA9_0==' ') ) {
            	            alt9=1;
            	        }


            	        switch (alt9) {
            	    	case 1 :
            	    	    // /home/victor/srec/core/src/main/antlr/srec.g:151:16: ' '
            	    	    {
            	    	    match(' '); 

            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop9;
            	        }
            	    } while (true);


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/victor/srec/core/src/main/antlr/srec.g:155:2: ( '#' ( . )* NEWLINE )
            // /home/victor/srec/core/src/main/antlr/srec.g:155:4: '#' ( . )* NEWLINE
            {
            match('#'); 
            // /home/victor/srec/core/src/main/antlr/srec.g:155:8: ( . )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='\r') ) {
                    alt11=2;
                }
                else if ( (LA11_0=='\n') ) {
                    alt11=2;
                }
                else if ( ((LA11_0>='\u0000' && LA11_0<='\t')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='\uFFFF')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /home/victor/srec/core/src/main/antlr/srec.g:155:8: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            skip();
            mNEWLINE(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // /home/victor/srec/core/src/main/antlr/srec.g:1:8: ( T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | BOOLEAN | NULL | ID | NUMBER | STRING | WS | NEWLINE | COMMENT )
        int alt12=15;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:10: T__28
                {
                mT__28(); 

                }
                break;
            case 2 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:16: T__29
                {
                mT__29(); 

                }
                break;
            case 3 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:22: T__30
                {
                mT__30(); 

                }
                break;
            case 4 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:28: T__31
                {
                mT__31(); 

                }
                break;
            case 5 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:34: T__32
                {
                mT__32(); 

                }
                break;
            case 6 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:40: T__33
                {
                mT__33(); 

                }
                break;
            case 7 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:46: T__34
                {
                mT__34(); 

                }
                break;
            case 8 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:52: BOOLEAN
                {
                mBOOLEAN(); 

                }
                break;
            case 9 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:60: NULL
                {
                mNULL(); 

                }
                break;
            case 10 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:65: ID
                {
                mID(); 

                }
                break;
            case 11 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:68: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 12 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:75: STRING
                {
                mSTRING(); 

                }
                break;
            case 13 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:82: WS
                {
                mWS(); 

                }
                break;
            case 14 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:85: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 15 :
                // /home/victor/srec/core/src/main/antlr/srec.g:1:93: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\1\13\4\uffff\5\13\6\uffff\7\13\1\36\1\37\2\13\1\42\1\13"+
        "\2\uffff\1\44\1\13\1\uffff\1\13\1\uffff\1\44\1\13\1\50\1\uffff";
    static final String DFA12_eofS =
        "\51\uffff";
    static final String DFA12_minS =
        "\1\11\1\145\4\uffff\1\145\1\156\1\162\1\141\1\151\6\uffff\1\161"+
        "\1\146\1\144\1\165\2\154\1\165\2\60\1\145\1\163\1\60\1\151\2\uffff"+
        "\1\60\1\145\1\uffff\1\162\1\uffff\1\60\1\145\1\60\1\uffff";
    static final String DFA12_maxS =
        "\1\172\1\145\4\uffff\1\145\1\156\1\162\1\141\1\151\6\uffff\1\161"+
        "\1\146\1\144\1\165\2\154\1\165\2\172\1\145\1\163\1\172\1\151\2\uffff"+
        "\1\172\1\145\1\uffff\1\162\1\uffff\1\172\1\145\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\2\uffff\1\2\1\3\1\4\1\5\5\uffff\1\12\1\13\1\14\1\15\1\16\1\17\15"+
        "\uffff\1\6\1\7\2\uffff\1\11\1\uffff\1\10\3\uffff\1\1";
    static final String DFA12_specialS =
        "\51\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\16\1\17\2\uffff\1\17\22\uffff\1\16\1\uffff\1\15\1\20\3\uffff"+
            "\1\15\1\2\1\3\2\uffff\1\4\3\uffff\12\14\3\uffff\1\5\3\uffff"+
            "\32\13\4\uffff\1\13\1\uffff\3\13\1\6\1\7\1\11\7\13\1\12\3\13"+
            "\1\1\1\13\1\10\6\13",
            "\1\21",
            "",
            "",
            "",
            "",
            "\1\22",
            "\1\23",
            "\1\24",
            "\1\25",
            "\1\26",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\1\40",
            "\1\41",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\1\43",
            "",
            "",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\1\45",
            "",
            "\1\46",
            "",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            "\1\47",
            "\12\13\7\uffff\32\13\4\uffff\1\13\1\uffff\32\13",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | BOOLEAN | NULL | ID | NUMBER | STRING | WS | NEWLINE | COMMENT );";
        }
    }
 

}