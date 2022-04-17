/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.apache.ibatis.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * mybatis只提供了DefaultReflectorFactory这一个实现类
 */
public class DefaultReflectorFactory implements ReflectorFactory {
  /**
   * 决定是否开启对Reflect对象的缓存
   */
  private boolean classCacheEnabled = true;

  /**
   * 使用率ConcurrentMap集合实现对Reflector对象的缓存
   */
  private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap<Class<?>, Reflector>();

  public DefaultReflectorFactory() {
  }

  @Override
  public boolean isClassCacheEnabled() {
    return classCacheEnabled;
  }

  @Override
  public void setClassCacheEnabled(boolean classCacheEnabled) {
    this.classCacheEnabled = classCacheEnabled;
  }

  @Override
  public Reflector findForClass(Class<?> type) {
    if (classCacheEnabled) {
      // synchronized (type) removed see issue #461
      Reflector cached = reflectorMap.get(type);
      if (cached == null) {
        cached = new Reflector(type);
        // 放入ConcurrentMap中进行缓存
        reflectorMap.put(type, cached);
      }
      return cached;
    } else {
      return new Reflector(type);
    }
  }

}
