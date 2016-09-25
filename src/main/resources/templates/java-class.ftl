<#macro javaValueClass
    fields
    name=conveyor.file.name
    package=conveyor.file.package
    startHash=conveyor.file.startHash>
package ${package};
<#if has_array(fields?values)>

import java.util.Arrays;
</#if>

public final class ${name} {
    <#list fields as fname, ftype>
    public final ${ftype} ${fname};
    </#list>

    public ${name}(<#list fields as fname, ftype>${ftype} ${fname}<#sep>, </#list>) {
        <#list fields?keys as fname>
        this.${fname} = ${fname};
        </#list>
    }

    <#nested>
    @Override
    public String toString() {
        return "${name}{"
            <#list fields as fname, ftype>
            + "${fname}=" + <@to_string type=ftype value=fname /> + ${fname?has_next?then('", "', '"}";')}
            </#list>
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ${name}) {
            ${name} that = (${name}) o;
            <#assign firstField = true>
            <#list fields as fname, ftype>
            <#assign cmp = comparison_expression(ftype)("this." + fname, "that." + fname)>
            ${firstField?string("return", "        &&")} ${cmp}${fname?has_next?string("", ";")}
            <#assign firstField = false>
            </#list>
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = ${startHash};
        <#list fields as fname, ftype>
        h *= 31;
        h ^= ${hash_expression(ftype)("this." + fname)};
        </#list>
        return h;
    }
}
</#macro>

<#assign primitiveTypes = [ "int", "short", "char", "byte", "long", "float", "double" ]>

<#function is_array type><#return type?ends_with("[]")></#function>
<#function is_primitive type><#return primitiveTypes?seq_contains(type)></#function>

<#function has_array type_list>
    <#list type_list as type>
        <#if is_array(type)>
            <#return true>
        </#if>
    </#list>
    <#return false>
</#function>

<#macro to_string type value><#if is_array(type)>Arrays.toString(${value})<#else>${value}</#if></#macro>

<#function float_bits x><#return "Float.floatToIntBits(" + x +")"></#function>
<#function double_bits x><#return "Double.doubleToLongBits(" + x +")"></#function>
<#function identity x><#return x></#function>

<#function compare_primitives lhs rhs><#return lhs + " == " + rhs></#function>
<#function compare_floats lhs rhs><#return float_bits(lhs) + " == " + float_bits(rhs)></#function>
<#function compare_doubles lhs rhs><#return double_bits(lhs) + " == " + double_bits(rhs)></#function>
<#function compare_arrays lhs rhs><#return "Arrays.equals(" + lhs + ", " + rhs + ")"></#function>
<#function compare_objects lhs rhs><#return lhs + ".equals(" + rhs + ")"></#function>

<#function hash_method x><#return x + ".hashCode()"></#function>
<#function hash_boolean x><#return x + "? 1231 : 1237"></#function>
<#function hash_long x><#return "(int) (" + x + " ^ (" + x + " >>> 32))"></#function>
<#function hash_double x><#return hash_long(double_bits(x))></#function>
<#function hash_array a><#return "Arrays.hashCode(" + a + ")"></#function>

<#assign primitiveComparison = {
    "float": compare_floats,
    "double": compare_doubles }>

<#assign hashCodeValue = {
    "int": identity,
    "short": identity,
    "char": identity,
    "byte": identity,
    "boolean": hash_boolean,
    "long": hash_long,
    "float": float_bits,
    "double": hash_double }>

<#function comparison_expression type>
    <#if is_array(type)>
        <#return compare_arrays>
    </#if>
    <#if is_primitive(type)>
        <#return primitiveComparison[type]!compare_primitives>
    </#if>
    <#return compare_objects>
</#function>

<#function hash_expression type>
    <#if is_array(type)>
        <#return hash_array>
    </#if>
    <#return hashCodeValue[type]!hash_method>
</#function>
