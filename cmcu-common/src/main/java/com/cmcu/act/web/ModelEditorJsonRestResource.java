/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmcu.act.web;

import com.cmcu.common.util.JsonMapper;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author Tijs Rademakers
 */
@RestController
@RequestMapping("/act/service")
public class ModelEditorJsonRestResource implements ModelDataJsonConstants {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
  
  @Resource
  RepositoryService repositoryService;
  

  @RequestMapping(value="/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
  public ObjectNode getEditorJson(@PathVariable String modelId) {
    ObjectNode modelNode = null;
    
    Model model = repositoryService.getModel(modelId);
      
    if (model != null) {
      try {
        if (StringUtils.isNotEmpty(model.getMetaInfo())) {
          modelNode = (ObjectNode) JsonMapper.objectMapper.readTree(model.getMetaInfo());
        } else {
          modelNode = JsonMapper.objectMapper.createObjectNode();
          modelNode.put(MODEL_NAME, model.getName());
        }
        modelNode.put("tenantId", model.getTenantId());
        modelNode.put(MODEL_ID, model.getId());
        ObjectNode editorJsonNode = (ObjectNode) JsonMapper.objectMapper.readTree(
                new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
        modelNode.put("model", editorJsonNode);
      } catch (Exception e) {
        LOGGER.error("Error creating model JSON", e);
        throw new ActivitiException("Error creating model JSON", e);
      }
    }
    return modelNode;
  }


  @ResponseBody
  @GetMapping(value="/editor/stencilset")
  public Object getStencilset(HttpServletRequest request) {
    try {


      //spring boot 使用
      ClassPathResource classPathResource = new ClassPathResource("static/editor-app/stencilset.json");
      InputStream stencilsetStream = classPathResource.getInputStream();
      //InputStream stencilsetStream = new FileInputStream(request.getServletContext().getRealPath("/editor-app/") + "stencilset.json");
      String result = IOUtils.toString(stencilsetStream, "utf-8");
      return JsonMapper.string2Map(result);
    } catch (Exception e) {
      throw new ActivitiException("Error while loading stencil set", e);
    }
  }


}


