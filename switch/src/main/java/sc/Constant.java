package sc;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Constant {
	public enum MarkType{
		MUST;
	}
	public enum ParamType{
		STRING("string"),
		NUMBER("number"),
		OBJECT("object"),
		BOOLEAN("boolean"),
		ARRAY_NUMBER("array<number>"),
		ARRAY_STRING("array<string>"),
		ARRAY_OBJECT("array<object>"),
		ARRAY_BOOLEAN("array<boolean>"),
		ARRAY("array");
		private String name;
		private ParamType(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public static ParamType getParamType(String name) {
			if (StringUtils.isEmpty(name)) {
				return null;
			}
			name = name.replace("<", "_");
			name = name.replace(">", "");
			return ParamType.valueOf(name.toUpperCase());
		}
		public static boolean verify(ParamType paramType,String target) {
			if (StringUtils.isEmpty(target)) {
				return false;
			}
			boolean result =false;
			switch (paramType.ordinal()) {
			case 0:
				result = true;
				break;
			case 1:
				result = Pattern.compile("^(-?[1-9]\\d*)||([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$").matcher(target).matches();
				break;
			case 2:
				result = true;
				break;
			case 3:
				result =  target.equals("true") || target.equals("false") ||target.equals("0") ||target.equals("1");
				break;
			case 4:
				String[] split = target.split(",");
				result =true;
				if (split.length > 0 && !target.endsWith(",")) {
					for (String s: split) {
						if (!(Pattern.compile("^(-?[1-9]\\d*)||([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$").matcher(s).matches())) {
							result = false;
							break;
						}
					}
				} else {
					result = false;
				}
				break ;
			case 5:
				result = target.split(",").length > 0 && !target.endsWith(",");
				break;
			case 6:
				result = target.split(",").length > 0 && !target.endsWith(",");
				break;
			case 7:
				result = target.split(",").length > 0 && !target.endsWith(",");
				break;
			case 8:
				result = target.split(",").length > 0 && !target.endsWith(",");
				break;
			default:
				break;
			}
			return result;
		}
	}
	public static void main(String[] args) {
		for (ParamType p: ParamType.values()) {
			System.out.println(ParamType.getParamType(p.getName()).getName());
		}
		System.out.println(ParamType.verify(ParamType.ARRAY_NUMBER, "1,2.0"));
	}
}
