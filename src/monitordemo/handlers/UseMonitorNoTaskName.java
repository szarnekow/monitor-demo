package monitordemo.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class UseMonitorNoTaskName extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		try {
			new ProgressMonitorDialog(window.getShell()).run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					int max = 100000000;
					SubMonitor convert = SubMonitor.convert(monitor, max);
					int i = 0;
					long result = 0;
					while(i < max) {
						result += i;
						i++;
						convert.worked(1);
						if (convert.isCanceled()) {
							throw new InterruptedException();
						}
					}
					final long showMe = result;
					window.getShell().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							MessageDialog.openInformation(
									window.getShell(),
									"Result",
									Long.toString(showMe));
						}
						
					});
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
