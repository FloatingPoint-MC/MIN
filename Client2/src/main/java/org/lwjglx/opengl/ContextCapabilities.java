package org.lwjglx.opengl;

import java.lang.reflect.Field;

public class ContextCapabilities {

    org.lwjgl.opengl.GLCapabilities cap = org.lwjgl.opengl.GL.getCapabilities();

    public ContextCapabilities() {

        Field[] fields = org.lwjgl.opengl.GLCapabilities.class.getFields();

        try {
            for (Field field : fields) {

                String name = field.getName();

                if (name.startsWith("GL_") || name.startsWith("OpenGL")) {

                    boolean value = field.getBoolean(cap);

                    try {
                        Field f = this.getClass().getField(name);
                        f.setBoolean(this, value);
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean GL_ARB_arrays_of_arrays;
    public boolean GL_ARB_base_instance;
    public boolean GL_ARB_blend_func_extended;
    public boolean GL_ARB_clear_buffer_object;
    public boolean GL_ARB_color_buffer_float;
    public boolean GL_ARB_compatibility;
    public boolean GL_ARB_compressed_texture_pixel_storage;
    public boolean GL_ARB_compute_shader;
    public boolean GL_ARB_copy_buffer;
    public boolean GL_ARB_copy_image;
    public boolean GL_ARB_debug_output;
    public boolean GL_ARB_depth_buffer_float;
    public boolean GL_ARB_depth_clamp;
    public boolean GL_ARB_depth_texture;
    public boolean GL_ARB_draw_buffers;
    public boolean GL_ARB_draw_buffers_blend;
    public boolean GL_ARB_draw_elements_base_vertex;
    public boolean GL_ARB_draw_indirect;
    public boolean GL_ARB_draw_instanced;
    public boolean GL_ARB_explicit_attrib_location;
    public boolean GL_ARB_explicit_uniform_location;
    public boolean GL_ARB_fragment_layer_viewport;
    public boolean GL_ARB_fragment_program;
    public boolean GL_ARB_fragment_program_shadow;
    public boolean GL_ARB_fragment_shader;
    public boolean GL_ARB_framebuffer_object;
    public boolean GL_ARB_framebuffer_sRGB;
    public boolean GL_ARB_geometry_shader4;
    public boolean GL_ARB_gpu_shader5;
    public boolean GL_ARB_half_float_pixel;
    public boolean GL_ARB_half_float_vertex;
    public boolean GL_ARB_instanced_arrays;
    public boolean GL_ARB_map_buffer_alignment;
    public boolean GL_ARB_map_buffer_range;
    public boolean GL_ARB_multisample;
    public boolean GL_ARB_multitexture;
    public boolean GL_ARB_occlusion_query;
    public boolean GL_ARB_occlusion_query2;
    public boolean GL_ARB_pixel_buffer_object;
    public boolean GL_ARB_seamless_cube_map;
    public boolean GL_ARB_shader_objects;
    public boolean GL_ARB_shader_stencil_export;
    public boolean GL_ARB_shader_texture_lod;
    public boolean GL_ARB_shadow;
    public boolean GL_ARB_shadow_ambient;
    public boolean GL_ARB_stencil_texturing;
    public boolean GL_ARB_sync;
    public boolean GL_ARB_tessellation_shader;
    public boolean GL_ARB_texture_border_clamp;
    public boolean GL_ARB_texture_buffer_object;
    public boolean GL_ARB_texture_cube_map;
    public boolean GL_ARB_texture_cube_map_array;
    public boolean GL_ARB_texture_env_combine;
    public boolean GL_ARB_texture_non_power_of_two;
    public boolean GL_ARB_uniform_buffer_object;
    public boolean GL_ARB_vertex_blend;
    public boolean GL_ARB_vertex_buffer_object;
    public boolean GL_ARB_vertex_program;
    public boolean GL_ARB_vertex_shader;
    public boolean GL_EXT_bindable_uniform;
    public boolean GL_EXT_blend_equation_separate;
    public boolean GL_EXT_blend_func_separate;
    public boolean GL_EXT_blend_minmax;
    public boolean GL_EXT_blend_subtract;
    public boolean GL_EXT_draw_instanced;
    public boolean GL_EXT_framebuffer_multisample;
    public boolean GL_EXT_framebuffer_object;
    public boolean GL_EXT_framebuffer_sRGB;
    public boolean GL_EXT_geometry_shader4;
    public boolean GL_EXT_gpu_program_parameters;
    public boolean GL_EXT_gpu_shader4;
    public boolean GL_EXT_multi_draw_arrays;
    public boolean GL_EXT_packed_depth_stencil;
    public boolean GL_EXT_paletted_texture;
    public boolean GL_EXT_rescale_normal;
    public boolean GL_EXT_separate_shader_objects;
    public boolean GL_EXT_shader_image_load_store;
    public boolean GL_EXT_shadow_funcs;
    public boolean GL_EXT_shared_texture_palette;
    public boolean GL_EXT_stencil_clear_tag;
    public boolean GL_EXT_stencil_two_side;
    public boolean GL_EXT_stencil_wrap;
    public boolean GL_EXT_texture_3d;
    public boolean GL_EXT_texture_array;
    public boolean GL_EXT_texture_buffer_object;
    public boolean GL_EXT_texture_filter_anisotropic;
    public boolean GL_EXT_texture_integer;
    public boolean GL_EXT_texture_lod_bias;
    public boolean GL_EXT_texture_sRGB;
    public boolean GL_EXT_vertex_shader;
    public boolean GL_EXT_vertex_weighting;
    public boolean OpenGL11;
    public boolean OpenGL12;
    public boolean OpenGL13;
    public boolean OpenGL14;
    public boolean OpenGL15;
    public boolean OpenGL20;
    public boolean OpenGL21;
    public boolean OpenGL30;
    public boolean OpenGL31;
    public boolean OpenGL32;
    public boolean OpenGL33;
    public boolean OpenGL40;
    public boolean OpenGL41;
    public boolean OpenGL42;
    public boolean OpenGL43;
    public boolean OpenGL44;
    public boolean GL_NV_fog_distance;
    public boolean GL_NV_geometry_shader4;
}
