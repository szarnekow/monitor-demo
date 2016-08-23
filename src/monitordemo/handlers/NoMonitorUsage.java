package monitordemo.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class NoMonitorUsage extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		try {
			new ProgressMonitorDialog(window.getShell()).run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					final long begin = System.currentTimeMillis();
					System.out.println("NoMonitorUsage runnable Begin: " + begin);
					int max = MonitorConstants.MAX;
					int i = 0;
					long result = 0;
					while(i < max) {
						result += i;
						i++;
					}
					final long showMe = result;
					window.getShell().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							long e1 = System.currentTimeMillis();
							System.out.println("UseMonitor opening msg box: " + e1);
							System.out.println("UseMonitor time until msg box open: " + (e1 - begin));
							MessageDialog.openInformation(
									window.getShell(),
									"Result",
									Long.toString(showMe));
						}
						
					});
					long end = System.currentTimeMillis();
					System.out.println("NoMonitorUsage runnable End: " + end);
					System.out.println("NoMonitorUsage runnable Duration: " + (end - begin));
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
