package arch.galaxyeclipse.client.resources;

class CachingResourceLoaderFactory implements IResourceLoaderFactory {
	private static CachingResourceLoader instance;
	
	public CachingResourceLoaderFactory() {
		
	}
	
	@Override
	public IResourceLoader createResourceLoader() {
		if (instance == null) {
			instance = new CachingResourceLoader();
		}
		return instance;
	}
}
