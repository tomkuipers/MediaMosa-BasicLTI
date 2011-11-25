package nl.uva.mediamosa.basiclti.service;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import nl.uva.mediamosa.basiclti.web.model.BasicLTIResource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for processing BasicLTIResources.
 * <p>
 * For a complete reference to Spring JDBC and JdbcTemplate
 * see http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/jdbc.html
 * <p>
 * For transactions, see http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/transaction.html
 */
@Service("btliResourceService")
@Transactional
public class BasicLTIResourceService {

	protected static Logger logger = Logger.getLogger("BasicLTIResourceService");
	private SimpleJdbcTemplate jdbcTemplate;
	
	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	/**
	  * Retrieves all bltiresources
	  *
	  * @return a list of bltiresources
	  */
	public List<BasicLTIResource> getAll() {
		logger.debug("Retrieving all BasicLTIResources");
		
		String sql = "select id, assetId, resourceLinkId, contextId, title, description from BasicLTIResource";
		
		RowMapper<BasicLTIResource> mapper = new RowMapper<BasicLTIResource>() {
			public BasicLTIResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				BasicLTIResource bltiResource = new BasicLTIResource();
				bltiResource.setId(rs.getInt("id"));
				bltiResource.setAssetId(rs.getString("assetId"));
				bltiResource.setResourceLinkId(rs.getString("resourceLinkId"));
				bltiResource.setContextId(rs.getString("contextId"));
				bltiResource.setTitle(rs.getString("title"));
				bltiResource.setDescription(rs.getString("description"));
				return bltiResource;
			}
		};
		// Retrieve all
		return  jdbcTemplate.query(sql, mapper);
	}
	
	public List<BasicLTIResource> getAllForContext(String resourceLinkId, String contextId) {
		logger.debug("Retrieving BasicLTIResources for contextId [" + contextId + "] and resourceLinkId [" + resourceLinkId + "]");
		
		String sql = "select id, assetId, resourceLinkId, contextId, title, description from BasicLTIResource " +
			"where resourceLinkId = :resourceLinkId and contextId = :contextId";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("resourceLinkId", resourceLinkId);
		parameters.put("contextId", contextId);

		
		RowMapper<BasicLTIResource> mapper = new RowMapper<BasicLTIResource>() {
			public BasicLTIResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				BasicLTIResource bltiResource = new BasicLTIResource();
				bltiResource.setId(rs.getInt("id"));
				bltiResource.setAssetId(rs.getString("assetId"));
				bltiResource.setResourceLinkId(rs.getString("resourceLinkId"));
				bltiResource.setContextId(rs.getString("contextId"));
				bltiResource.setTitle(rs.getString("title"));
				bltiResource.setDescription(rs.getString("description"));
				return bltiResource;
			}
		};
		// Retrieve all used in specific course
		return jdbcTemplate.query(sql, mapper, parameters);
	}

	public List<BasicLTIResource> getResource(Integer id) {
		
		String sql = "select id, assetId, resourceLinkId, contextId, title, description from BasicLTIResource where id = ?";
		
		Object[] parameters = new Object[] {id};
		
		RowMapper<BasicLTIResource> mapper = new RowMapper<BasicLTIResource>() {
			public BasicLTIResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				BasicLTIResource bltiResource = new BasicLTIResource();
				bltiResource.setId(rs.getInt("id"));
				bltiResource.setAssetId(rs.getString("assetId"));
				bltiResource.setResourceLinkId(rs.getString("resourceLinkId"));
				bltiResource.setContextId(rs.getString("contextId"));
				bltiResource.setTitle(rs.getString("title"));
				bltiResource.setDescription(rs.getString("description"));
				return bltiResource;
			}
		};
		return jdbcTemplate.query(sql, mapper, parameters);
	}
	
	
	/**
	  * Adds a new bltiresource
	  *
	  * @param assetId the assetId of the video used in the basicltiresource
	  * @param resourceLinkId the resourceLinkId of the basicltiresource
	  * @param contextId the contextId of the basicltiresource
	  * @param title the title of the basicltiresource
	  * @param description the description of the basicltiresource
	  */
	 public void add(String assetId, String resourceLinkId, String contextId, String title, String description) {
		 logger.debug("Adding new basicltiresource");
		 
		 // Prepare our SQL statement using Named Parameters style
		 String sql = "insert into BasicLTIResource(assetId, resourceLinkId, contextId, title, description) values " +
		 	"(:assetId, :resourceLinkId, :contextId, :title, :description)";
		 
		 // Assign values to parameters
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("assetId", assetId);
		 parameters.put("resourceLinkId", resourceLinkId);
		 parameters.put("contextId", contextId);
		 parameters.put("title", title);
		 parameters.put("description", description);
		 
		 // Save
		 jdbcTemplate.update(sql, parameters);
	 }
	 
	 /**
	  * Deletes an existing basicltiresource
	  * @param id the id of the existing basicltiresource
	  */
	 public void delete(Integer id) {
		 logger.debug("Deleting existing basicltiresource");
		 
		 // Prepare our SQL statement using Unnamed Parameters style
		 String sql = "delete from BasicLTIResource where id = ?";
		 
		 // Assign values to parameters
		 Object[] parameters = new Object[] {id};
		 
		 // Delete
		 jdbcTemplate.update(sql, parameters);
	 }
	 
	 /**
	  * Edits an existing basicltiresource
	  * @param assetId the assetId of the existing basicltiresource
	  * @param resourceLinkId the resourceLinkId of the existing basicltiresource
	  * @param contextId the contextId of the existing basicltiresource
	  * @param title the title of the existing basicltiresource
	  * @param description the description of the existing basicltiresource
	  */
	 public void edit(Integer id, String assetId, String resourceLinkId, String contextId, String title, String description ) {
		 logger.debug("Editing existing basicltiresource");
		 
		 // Prepare our SQL statement
		 String sql = "update BasicLTIResource set assetId = :assetId, " +
		 	"resourceLinkId = :resourceLinkId, contextId = :contextId, title = :title, description = :description where id = :id";
		 
		 // Assign values to parameters
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("id", id);
		 parameters.put("assetId", assetId);
		 parameters.put("resourceLinkId", resourceLinkId);
		 parameters.put("contextId", contextId);
		 parameters.put("title", title);
		 parameters.put("description", description);
		 
		 // Edit
		 jdbcTemplate.update(sql, parameters);
	 }

}
