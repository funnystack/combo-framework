package com.funny.combo.trade.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * @author  fangli
 * @date 创建时间：2017年7月21日 下午5:25:47 
 * 处理非法字符
 */
public class XSSShieldUtil {

	private static List<Pattern> patterns = null;

    private static List<Object[]> getXssPatternList() {
        List<Object[]> ret = new ArrayList<Object[]>();
        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        return ret;
    }

    private static List<Pattern> getPatterns() {
        if (patterns == null) {
            List<Pattern> list = new ArrayList<Pattern>();
            String regex = null;
            Integer flag = null;
            int arrLength = 0;
            for(Object[] arr : getXssPatternList()) {
                arrLength = arr.length;
                for(int i = 0; i < arrLength; i++) {
                    regex = (String)arr[0];
                    flag = (Integer)arr[1];
                    list.add(Pattern.compile(regex, flag));
                }
            }
            patterns = list;
        }
        return patterns;
    }

    public static String stripXss(String value) {
        if(StringUtils.isNotBlank(value)) {
            Matcher matcher = null;
            for(Pattern pattern : getPatterns()) {
                matcher = pattern.matcher(value);
                // 匹配
                if(matcher.find()) {
                    // 删除相关字符串
                    value = matcher.replaceAll("");
                }
            }
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return value;
    }

}
