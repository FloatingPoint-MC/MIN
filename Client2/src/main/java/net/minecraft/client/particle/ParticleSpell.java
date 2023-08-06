package net.minecraft.client.particle;

import java.util.Random;

import net.minecraft.world.World;

public class ParticleSpell extends Particle {
    private static final Random RANDOM = new Random();

    /**
     * Base spell texture index
     */
    private int baseSpellTextureIndex = 128;

    protected ParticleSpell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1229_8_, double ySpeed, double p_i1229_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), ySpeed, 0.5D - RANDOM.nextDouble());
        this.motionY *= 0.2D;

        if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D) {
            this.motionX *= 0.1D;
            this.motionZ *= 0.1D;
        }

        this.particleScale *= 0.75F;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    public boolean shouldDisableDepth() {
        return true;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        this.setParticleTextureIndex(this.baseSpellTextureIndex + (7 - this.particleAge * 8 / this.particleMaxAge));
        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.96D;
        this.motionY *= 0.96D;
        this.motionZ *= 0.96D;

        if (this.onGround) {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
        }
    }

    /**
     * Sets the base spell texture index
     */
    public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn) {
        this.baseSpellTextureIndex = baseSpellTextureIndexIn;
    }

    public static class AmbientMobFactory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setAlphaF(0.15F);
            particle.setRBGColorF((float) xSpeedIn, (float) ySpeedIn, (float) zSpeedIn);
            return particle;
        }
    }

    public static class Factory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

    public static class InstantFactory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell) particle).setBaseSpellTextureIndex(144);
            return particle;
        }
    }

    public static class MobFactory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            particle.setRBGColorF((float) xSpeedIn, (float) ySpeedIn, (float) zSpeedIn);
            return particle;
        }
    }

    public static class WitchFactory implements IParticleFactory {
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            Particle particle = new ParticleSpell(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
            ((ParticleSpell) particle).setBaseSpellTextureIndex(144);
            float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
            particle.setRBGColorF(f, 0.0F * f, f);
            return particle;
        }
    }
}
