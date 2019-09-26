__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void copybuffer
(
  __global uchar* src, __global uchar* dst
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int w = get_global_size(0), h = get_global_size(1);

  int pos = i + j * w + k * w * h;

  dst[pos] = src[pos];
}
