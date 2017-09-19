package com.example.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fzy on 2017/9/19.
 */
public class TemplateUtils {
    public static String generate(Object object, String templateId, String templateContent) {
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        //String templateContent="欢迎：${name}";
        stringLoader.putTemplate(templateId,templateContent);
        cfg.setTemplateLoader(stringLoader);

        try {
            Template template = cfg.getTemplate(templateId, "utf-8");
            Map root = new HashMap();
            root.put("object", object);
            StringWriter writer = new StringWriter();
            try {
                template.process(root, writer);
                return writer.toString();
            } catch (TemplateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
