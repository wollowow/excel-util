# excel-util
## 概述
封装了poi框架，实现excel的导入导出功能，导入功能可以直接将excel文件转化成对象的list,导出功能可以将对象列表转化成workbook对象然后通过流进行传输
## 导入功能<br>
### 1.功能介绍
通过传入文件，文件流，文件路径等方式导入excel文件，解析并校验文件中数据然后生成List<T>。适用于第一行为标题行，其他行为数据行的excel文件格式，不适用复杂格式的excel文件解析。
### 2.功能使用
1）添加注解
需要导出的字段要在对应属性上添加 @InventorField注解，不添加注解的字段不会进行解析。
```
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InventorField {
    String  name() default "";                              //中文名字
    boolean required() default false;                       //校验是否必填
    String regex() default "";                              //自定义正则校验
    String format() default "";                             //校验日期格式
    Class<? extends CustomCheckRule> custom() default CustomCheckRule.class;             //自定义校验规则

}
```
名称 | 属性类型 | 可添加字段类型 | 默认值 | 是否必填 | 说明 |
:-: | :-: | :-: | :-: | :-: | :-: |
name | String | 不限 | "" | 是 | 导入字段标题名称，需要和标题名称匹配
required | boolean| 不限 | true | 否 |是否必填字段
regex | String | 不限 | "" | 否 | 正则表达式校验
format | String | date | "" | 否 | 时间格式
custom | Class | 不限 | CustomCheckRule.class | 否 | 自定义校验规则


2）方法调用<br>
```
FileInputStream fileIn = new FileInputStream("###");
InventorParseResult parseResult = ExcelFactory.initInventor(fileIn, InventorBean.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR).parse();
File file = new File("###");
InventorParseResult parseResult = ExcelFactory.initInventor(file, InventorBean.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR).parse();

String filePath = "###"
InventorParseResult parseResult = ExcelFactory.initInventor(filePath, InventorBean.class,
                    CheckRuleEnum.BREAK_WHEN_ERROR).parse();

```
名称 | 类型 | 是否必填 | 说明 |
:-: | :-: | :-: | :-: |
fileIn | InputStream | 是 | 需要导入的文件流	
file | File | 是 | 需要导入的文件
filePath | String | 是 | 需要导入的文件路径
tClass | class| 是 | 需要转换成的对象(对应属性添加了InventorField注解并且需要继承InventorBeanTemplate类，校验错误信息会记录在InventorBeanTemplate类的errMsg属性中)
CheckRuleEnum | CheckRuleEnum | 否 | 解析出错处理方式 CONTINUE_WHEN_ERROR：校验出错之后跳过当前单元格继续校验；BREAK_WHEN_ERROR：校验出错之后直接抛异常
formater | CellValueFormater| 否 | 数据转换格式化类，可自定义，默认DefaultCellValueFormater

3）解析结果<br>
最终解析结果封装为InventorParseResult对象
```

    /**
     * 正确数据列表
     */
    private List<T> sucList;
    /**
     * 错误数据列表
     */
    private List<T> errList;

    /**
     * 处理结果 ：    
     *     ALL_SUCCESS(), //全部成功
     *     ALL_FAIL(), //全部失败
     *     PARTIAL_FAILURE(); //部分失败
     */
    private ParseResultEnum parseResultEnum;
    
    public InventorParseResult(List<T> sucList, List<T> errList) {
        this.errList = errList;
        this.sucList = sucList;
        if (CollectionUtils.isEmpty(errList)) {
            this.parseResultEnum = ParseResultEnum.ALL_SUCCESS;
        } else if (CollectionUtils.isEmpty(sucList)) {
            this.parseResultEnum = ParseResultEnum.ALL_FAIL;
        } else {
            this.parseResultEnum = ParseResultEnum.PARTIAL_FAILURE;
        }
    }
    public List<T> getSucList() {
        return sucList;
    }

    public List<T> getErrList() {
        return errList;
    }

    public ParseResultEnum getParseResultEnum() {
        return parseResultEnum;
    }
```




## 导出功能<br>
### 1.功能介绍
导出功能主要通过调用方传入导出对象的List，根据要导出数据对象的注解返回excel的workbook对象,将workbook对象写入

特定的输出流可以实现excel文件的导出。

### 2.功能使用
1）添加注解
需要导出的字段要在对应属性上添加 @ExportField注解，不添加注解的字段不会被导出。
```
/**
 * 导出字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportField {

    String name() default "";  //导出标题名称
    String format() default ""; //格式化日期
    int scale() default 0;  //数字保留小数点
    String prefix() default ""; //导出数据前缀
    String suffix() default ""; //导出数据后缀
    boolean percent() default false; //百分比输出
    Class<? extends ISEnum> enumRule() default ISEnum.class; //注解数据处理
    Class<? extends CustomRule> custom() default CustomRule.class; //自定义规则
}
```

名称 | 属性类型 | 可添加字段类型 | 默认值 | 是否必填 | 说明 |
:-: | :-: | :-: | :-: | :-: | :-: |
name | String | 不限 | "" | 否| 导出字段标题名称，不配的情况下取属性名称
format | String| date | "" | 否 |格式化时间
scale | int | Number | 0 | 否 | 保留小数长度，和percent共同配置时为百分比数字部分保留小数长度
percent | boolean | Number | false | 否 | 百分比方式显示数字，和scale共同配置时限制百分比数字部分保留小数长度
prefix | String | 不限 | "" | 否 | 导出数据前缀
suffix | String | 不限 | "" | 否 | 导出数据后缀
custom | Class | 不限 | CustomRule.class | 否 | 用户自定义规则，需要实现CustomRule接口
enumRule | Class | Integer | ISEnum.class | 否 | 枚举类型数据导出，需要实现ISEnum接口

2）方法调用<br>
Workbook workbook = ExcelFactory.initExporter(list, ExcelEnum.EXCEL_2007,columns).generalExport();

名称 | 类型 | 是否必填 | 说明 |
:-: | :-: | :-: | :-: |
list | List<T> | 是 | 需要导出的数据集合	
ExcelEnum | ExcelEnum | 否 | 指定导出方式，是按照xls格式导出还是xlsx格式导出（默认xlsx格式），如果需要导出的数据超过xls文件最大支持数据则按照xlsx方式导出
columns | List<String> | 否 |需要导出的属性名称，不传的情况下按照T对象的属性顺序导出所有配置了注解的数据。columns中不能重复，不能添加T中不存在的属性名称且要保证columns中所有属性名称都配置了注解。导出列的顺序和columns一致

3.注意事项<br>
1）导出的excel列顺序默认按照对应bean的属性顺序，如要自定义列顺序或者自定义导出哪些列，需要传入属性名称的List,并且List中
所包含的属性都要配置了@ExportField注解<br>

2）数据量较大时建议用ExcelEnum.EXCEL_2007的方式进行导出，可以防止内存溢出问题,数据量超过xls最大数据条数（65536）时
会强制按照ExcelEnum.EXCEL_2007方式导出。生成文件的后缀名可通过exporter.getExcelEnum().getSuffix();获取

3）导出注解的percent参数使用时会在原有的数字基础上乘以100，因此如果记录的数字是百分数的数字部分则不能使用该参数。可以用suffix="%"来添加百分号

----
用法可参考com.angla.demo.excel

