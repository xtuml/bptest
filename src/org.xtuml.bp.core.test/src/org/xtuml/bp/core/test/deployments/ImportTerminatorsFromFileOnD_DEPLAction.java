package org.xtuml.bp.core.test.deployments;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.core.Deployment_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Util_c;
import org.xtuml.bp.core.ui.Selection;
import org.xtuml.bp.core.util.TransactionUtil;

/* 
 * This helper class is a copy of ImportTerminatorsFromFileOnD_DEPLAction from
 * org.xtuml.bp.core.ui except that it allows the tester to pass in the
 * selection list instead of popping a dialog.
 */

public class ImportTerminatorsFromFileOnD_DEPLAction implements IObjectActionDelegate {
    
    private Object v_file_selection;

    /**
     * Constructor for ImportTerminatorsFromFileOnD_DEPLAction.
     */
    public ImportTerminatorsFromFileOnD_DEPLAction(Iterator<String> file_selection) {
        super();
        v_file_selection = file_selection;
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // The part is mainly needed to locate the selection provider, and
        // we cache our selection in core so no action is needed here.
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        IStructuredSelection structuredSelection = Selection.getInstance().getStructuredSelection();
        D_DEPL_ImportTerminatorsFromFile(structuredSelection);

    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    private void D_DEPL_ImportTerminatorsFromFile(IStructuredSelection selection) {
        // Assign the context selection variables with the action context
        // Assign the context selection variable with the action context
        Object context = selection.iterator().next();
        Deployment_c v_element = (Deployment_c) context;

        TransactionUtil.TransactionGroup transactionGroup = TransactionUtil
                .startTransactionsOnSelectedModelRoots("-- Import terminators from file");
        try {
            // Ensure that actions take place between Verifier Activity executions
            Ooaofooa.beginSaveOperation();
            if ((v_element != null)) {

                String v_filename = Util_c.Getnextstring(v_file_selection);

                while ((!"".equals(v_filename))) {
                    if (v_element != null) {
                        v_element.Importfromfile(v_filename);
                    } else {
                        Throwable t = new Throwable();
                        t.fillInStackTrace();
                        CorePlugin.logError("Attempted to call an operation on a null instance.", t);
                    }

                    v_filename = Util_c.Getnextstring(v_file_selection);

                }
            }

            // end critical section
            Ooaofooa.endSaveOperation();
            // catch all exceptions and cancel the transactions
        } catch (Exception e) {
            Ooaofooa.endSaveOperation();
            TransactionUtil.cancelTransactions(transactionGroup, e);
            CorePlugin.logError("Transaction: -- Import terminators from file failed", e);//$NON-NLS-1$
        }
        TransactionUtil.endTransactions(transactionGroup);
    }
} // end ImportTerminatorsFromFileOnD_DEPLAction
