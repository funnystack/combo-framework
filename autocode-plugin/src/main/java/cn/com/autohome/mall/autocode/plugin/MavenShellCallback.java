/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package cn.com.autohome.mall.autocode.plugin;

import java.io.File;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * @author Jeff Butler
 */
public class MavenShellCallback extends DefaultShellCallback {
    private MyBatisGeneratorMojo mybatisGeneratorMojo;

    /**
     * @param overwrite
     */
    public MavenShellCallback(MyBatisGeneratorMojo mybatisGeneratorMojo, boolean overwrite) {
        super(overwrite);
        this.mybatisGeneratorMojo = mybatisGeneratorMojo;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        if (targetPackage.contains(".")) {
            String[] dirs = targetPackage.split(".");
            for (String dir : dirs) {
                targetProject = targetProject + File.separator + dir;
            }
        } else {
            targetProject = targetProject + File.separator + targetPackage;
        }
        String realPath = targetProject;
        File target = new File(realPath);
        if (!target.exists()) {
            target.mkdir();
        }
        return super.getDirectory(targetProject, targetPackage);
    }

}
