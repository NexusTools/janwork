/*
 * janxutils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 or any later version.
 * 
 * janxutils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with janxutils.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.nexustools.janet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import net.nexustools.concurrent.Prop;
import net.nexustools.data.accessor.PropAccessor;
import net.nexustools.concurrent.logic.SoftWriteReader;
import net.nexustools.data.adaptor.Adaptor;
import net.nexustools.data.adaptor.AdaptorException;
import net.nexustools.data.annote.ThreadUnsafe;
import net.nexustools.io.DataInputStream;
import net.nexustools.io.DataOutputStream;
import net.nexustools.io.MemoryStream;
import net.nexustools.utils.NXUtils;
import net.nexustools.utils.log.Logger;

/**
 *
 * @author kate
 */
@ThreadUnsafe
public abstract class Packet<T, C extends Client, S extends Server> {
	
	private final Prop<byte[]> cache = new Prop();
	
	protected abstract void recvFromServer(C client);
	protected abstract void recvFromClient(C client, S server);
	
	protected void aboutToSend(C client) {}
	protected void sendComplete(C client) {}
	protected void failedToComplete(C client, Throwable reason) {}
	
	public void read(DataInputStream dataInput, C client) throws UnsupportedOperationException, IOException {
		try {
			Adaptor.resolveAndRead(this, dataInput);
		} catch (AdaptorException ex) {
			Logger.warn("Don't call super.read on your packet if you don't intend to use an adaptor");
			throw new IOException(ex);
		}
	}
	public void write(DataOutputStream dataOutput, C client) throws UnsupportedOperationException, IOException {
		try {
			Adaptor.resolveAndWrite(this, dataOutput);
		} catch (AdaptorException ex) {
			Logger.exception(ex);
			Logger.warn("Packet may be corrupt, don't call super.read on your packet if you don't intend to use an adaptor");
		}
	}
	public byte[] data(final C client) throws IOException {
		try {
			return cache.read(new SoftWriteReader<byte[], PropAccessor<byte[]>>() {
				@Override
				public byte[] soft(PropAccessor<byte[]> data) {
					return data.get();
				}
				@Override
				public byte[] read(PropAccessor<byte[]> data) throws IOException {
					MemoryStream memoryStream = new MemoryStream();
					write(memoryStream.createDataOutputStream(), client);
					data.set(memoryStream.toByteArray());
					return data.get();
				}
			});
		} catch (InvocationTargetException ex) {
			throw NXUtils.wrapRuntime(ex);
		}
	}
	
	private ClientStorage<T> storage;
	public ClientStorage<T> storage() {
		if(storage == null)
			storage = new ClientStorage<T>(getClass());
		return storage;
	}
	
}
