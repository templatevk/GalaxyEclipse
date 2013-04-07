package arch.galaxyeclipse.client.texture;

class CachingTextureAtlasFactory implements ITextureAtlasFactory {
	private static CachingTextureAtlas instance;
	
	public CachingTextureAtlasFactory() {
		
	}
	
	@Override
	public ITextureAtlas createAtlas() {
		if (instance == null) {
			instance = new CachingTextureAtlas();
		}
		return instance;
	}
}
