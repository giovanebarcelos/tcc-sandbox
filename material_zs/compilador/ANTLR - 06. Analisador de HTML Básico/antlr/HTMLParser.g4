parser grammar HTMLParser;

options { tokenVocab=HTMLLexer; }

document
    : element_list EOF
    ;

element_list
    : (element)*
    ;

element
    : tag
    | TEXT
    ;

tag
    : TAG_OPEN NAME attributes TAG_CLOSE element_list TAG_END_OPEN NAME TAG_CLOSE
    ;

attributes
    : (attribute)*
    ;

attribute
    : NAME EQ ATTR_VALUE
    ;
