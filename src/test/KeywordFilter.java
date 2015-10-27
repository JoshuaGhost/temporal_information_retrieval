package test;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.IOFileFilter;

public class KeywordFilter implements FileFilter, IOFileFilter{
	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}
}
