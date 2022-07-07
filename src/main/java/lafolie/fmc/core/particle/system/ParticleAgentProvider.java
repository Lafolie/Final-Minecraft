package lafolie.fmc.core.particle.system;

/**
 * Used by objects that have ParticleAgents
 */
public interface ParticleAgentProvider
{
	/**
	 * Called when the object is loaded
	 */
	public void initParticleAgents();

	/**
	 * Called when the object is unloaded
	 */
	public void stopParticleAgents();

	/**
	 * Called when the object is no longer rendered
	 */
	public void autoStopParticleAgents();

	/**
	 * Called when the object is rendered
	 */
	public void autoPlayParticleAgents();
}
