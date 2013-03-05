package dtool.ast;


public enum ASTNodeTypes {
	@Deprecated
	OTHER,
	
	SYMBOL,
	DEF_UNIT,
	
	MODULE,
	DECL_MODULE,
	DECL_IMPORT,
	IMPORT_CONTENT,
	IMPORT_ALIAS,
	IMPORT_SELECTIVE,
	IMPORT_SELECTIVE_ALIAS,
	
	DECL_EMTPY,
	DECL_INVALID,
	INVALID_SYNTAX,
	NODE_LIST,
	
	//-- various Declarations
	DECL_LINKAGE,
	DECL_ALIGN,
	DECL_PRAGMA,
	DECL_PROTECTION,
	DECL_BASIC_ATTRIB,
	
	DECL_MIXIN_STRING,
	
	/* ---------------------------------- */
	
	DEFINITION_VARIABLE,
	DEFINITION_VAR_FRAGMENT,
	INITIALIZER_EXP,
//	INITIALIZER_ARRAY,
//	INITIALIZER_STRUCT,
//	INITIALIZER_VOID,

	
	/* ---------------------------------- */
	
	REF_IMPORT_SELECTION,
	REF_IDENTIFIER,
	REF_QUALIFIED,
	REF_MODULE_QUALIFIED,
	REF_PRIMITIVE,
	REF_MODULE,
	
	REF_TYPE_DYN_ARRAY,
	REF_TYPE_POINTER,
	
	REF_INDEXING,
	REF_TEMPLATE_INSTANCE,
	REF_TYPEOF,
	REF_MODIFIER,
	
	
	/* ---------------------------------- */
	
	MISSING_EXPRESSION,
	EXP_REF_RETURN,
	
	EXP_THIS,
	EXP_SUPER,
	EXP_NULL,
	EXP_ARRAY_LENGTH,
	EXP_LITERAL_BOOL,
	EXP_LITERAL_INTEGER,
	EXP_LITERAL_STRING,
	EXP_LITERAL_CHAR,
	EXP_LITERAL_FLOAT,
	
	EXP_LITERAL_ARRAY,
	EXP_LITERAL_MAPARRAY,
	MAPARRAY_ENTRY,
	
	EXP_REFERENCE,
	EXP_PARENTHESES,
	
	EXP_ASSERT,
	EXP_MIXIN_STRING,
	EXP_IMPORT_STRING,
	EXP_TYPEID,
	
	EXP_INDEX,
	EXP_SLICE,
	EXP_CALL,
	
	EXP_PREFIX,
	EXP_NEW,
	EXP_CAST,
	EXP_CAST_QUAL,
	EXP_POSTFIX,
	EXP_INFIX,
	EXP_CONDITIONAL,
	
	/* ---------------------------------- */
	
	;
}
