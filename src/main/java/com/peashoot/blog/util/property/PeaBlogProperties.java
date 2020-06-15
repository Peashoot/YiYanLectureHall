package com.peashoot.blog.util.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "peashoot.blog")
public class PeaBlogProperties {
    /**
     * http请求通用配置项
     */
    private HttpProperties http;

    @Data
    private static class HttpProperties {
        /**
         * content配置项
         */
        private ContentProperties content;

        @Data
        private static class ContentProperties {
            /**
             * 编码方式
             */
            private String charset;
            /**
             * content解密配置项
             */
            private DecryptProperties decrypt;

            @Data
            private static class DecryptProperties {
                /**
                 * 是否启用
                 */
                private Boolean enabled;
                /**
                 * 加密密钥
                 */
                private String key;
            }

            /**
             * content加密配置项
             */
            private EncryptProperties encrypt;

            @Data
            private static class EncryptProperties {
                /**
                 * 是否启用
                 */
                private Boolean enabled;
                /**
                 * 加密密钥
                 */
                private String key;
            }

            /**
             * 通用参数缺失提示
             */
            private String paramsMissingNotice;
        }

        /**
         * jwt配置项
         */
        private JwtProperties jwt;

        @Data
        private static class JwtProperties {
            /**
             * 加密密钥
             */
            private String secret;
            /**
             * 有效期
             */
            private Long expiration;
            /**
             * token所在的请求头key值
             */
            private String header;
            /**
             * token开头内容
             */
            private String tokenHead;
            /**
             * token包含信息配置项
             */
            private ContainsProperties contains;

            @Data
            private static class ContainsProperties {
                /**
                 * token中是否包含访问ip
                 */
                private Boolean visitIp;
                /**
                 * token中是否包含浏览器指纹
                 */
                private Boolean browserFingerprint;
            }
        }

        /**
         * 申请信息配置项
         */
        private ApplyProperties apply;

        @Data
        private static class ApplyProperties {
            /**
             * 申请内容有效期
             */
            private Long expiration;
            /**
             * 幂等信息配置项
             */
            private IdempotentProperties idempotent;

            @Data
            private static class IdempotentProperties {
                /**
                 * 幂等时间间隔（单位：秒）
                 */
                private Long interval;
            }
        }
    }

    /**
     * 单点登录配置项
     */
    private SingleSignOnProperties sso;

    @Data
    private static class SingleSignOnProperties {
        /**
         * 是否启用
         */
        private Boolean enabled;
    }

    /**
     * 文件上传配置项
     */
    private FileProperties file;

    @Data
    private static class FileProperties {
        /**
         * 可上传最大文件大小（单位：kb）
         */
        private Integer maxFileSize;
        /**
         * 资源属性配置项
         */
        private ResourceProperties resource;

        @Data
        private static class ResourceProperties {
            /**
             * 文件路径配置项
             */
            private DirectoryProperties directory;

            @Data
            private static class DirectoryProperties {
                /**
                 * 本地文件访问url根路径
                 */
                private String net;
                /**
                 * 本地文件存储根目录
                 */
                private String local;
            }
        }
    }
}

