/**
 * Greedy_Host_Evacuation_in_Cloud - Technion, Israel Institute of Technology
 * 
 * Author: Assaf Israel, 2013
 * Created: Jun 26, 2013
 */
package il.ac.technion.glpk;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import static org.gnu.glpk.GLPK.*;
import org.gnu.glpk.*;
import org.junit.Test;

public class GLPKTests {
	
	private static Logger logger = Logger.getLogger(GLPKTests.class);

	@Test
	public void expectedGLPKversion() {
		assertEquals("4.51",glp_version());
	}

	/**
	 * Min z = x + y
	 * s.t 
	 * x >= 3
	 * y >= 7
	 * 
	 * y is natural
	 * x is free
	 * @throws Exception 
	 */
	@Test
	public void simpleMIP() throws Exception {		
		// Create problem
		glp_prob lp = glp_create_prob();
		logger.info("Problem created");
		glp_set_prob_name(lp,"Simple MIP");
		
		// Define columns (variables)
		logger.info("Defining variables: int x free, int y >= 0");
		glp_add_cols(lp,2);
		glp_set_col_name(lp,1,"x");
		glp_set_col_kind(lp,1,GLP_IV); // Integer Variable
		glp_set_col_bnds(lp,1,GLP_FR,0,0); // x is free
		glp_set_col_name(lp,2,"y");
		glp_set_col_kind(lp,2,GLP_IV); // Integer Variable
		glp_set_col_bnds(lp,2,GLP_LO,0,0); // y >= 0
		
		// Define constraints
		glp_add_rows(lp,2);
		
		logger.info("c1: 1x + 0y >= 3");
		glp_set_row_name(lp, 1, "c1");
		glp_set_row_bnds(lp, 1, GLP_LO, 3, 0); // aX >= 3
		
		SWIGTYPE_p_int ind = new_intArray(3);
		SWIGTYPE_p_double val = new_doubleArray(3);
		
		intArray_setitem(ind, 1, 1);
		intArray_setitem(ind, 2, 2);
		doubleArray_setitem(val,1,1);
		doubleArray_setitem(val,2,0);
		glp_set_mat_row(lp, 1, 2, ind, val); // 1x + 0y
		
		logger.info("c2: 0x + 1y >= 7");
		glp_set_row_name(lp, 2, "c2");
		glp_set_row_bnds(lp, 2, GLP_LO, 7, 0); // aY >= 7
		
		doubleArray_setitem(val,1,0);
		doubleArray_setitem(val,2,1);
		glp_set_mat_row(lp, 2, 2, ind, val); // 0x + 1y
		
		delete_doubleArray(val);
		delete_intArray(ind);
		
		//Define objective
		logger.info("objective: Min z = 1x + 1y");
		glp_set_obj_name(lp,"objective");
		glp_set_obj_dir(lp,GLP_MIN);
		glp_set_obj_coef(lp,0,0); // Seems like this should always be 0
		glp_set_obj_coef(lp,1,1);
		glp_set_obj_coef(lp,2,1); // 1x + 1y
		
		// Solve Model
		glp_iocp iocp = new glp_iocp();
		glp_init_iocp(iocp);
		iocp.setPresolve(GLP_ON);
		logger.info("Solving...");
		glp_write_mps(lp,GLP_MPS_FILE,null,"simpleProblem.mps");
		try {
			if (glp_intopt(lp,iocp) != 0) {
				logger.error("The problem could not be solved");
				throw new Exception("The problem could not be solved");
			}	
			logger.info("Solution found");
			GLPKUtils.writeMipSolution(lp);
			assertEquals(10,glp_mip_obj_val(lp),1e-5);
		} finally {
			logger.info("Clean up");
			glp_delete_prob(lp);
		}
	}
}
