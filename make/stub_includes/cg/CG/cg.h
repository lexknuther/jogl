/*
 * This is a modified version of the original header as provided by
 * NVidia; original copyright appears below.
 *
 * Modified by Christopher Kline, May 2003: Stripped down and hacked to get
 * around macro interpretation problems.
 */

/*
 *
 * Copyright (c) 2002, NVIDIA Corporation.
 * 
 *  
 * 
 * NVIDIA Corporation("NVIDIA") supplies this software to you in consideration 
 * of your agreement to the following terms, and your use, installation, 
 * modification or redistribution of this NVIDIA software constitutes 
 * acceptance of these terms.  If you do not agree with these terms, please do 
 * not use, install, modify or redistribute this NVIDIA software.
 * 
 *  
 * 
 * In consideration of your agreement to abide by the following terms, and 
 * subject to these terms, NVIDIA grants you a personal, non-exclusive license,
 * under NVIDIA�s copyrights in this original NVIDIA software (the "NVIDIA 
 * Software"), to use, reproduce, modify and redistribute the NVIDIA 
 * Software, with or without modifications, in source and/or binary forms; 
 * provided that if you redistribute the NVIDIA Software, you must retain the 
 * copyright notice of NVIDIA, this notice and the following text and 
 * disclaimers in all such redistributions of the NVIDIA Software. Neither the 
 * name, trademarks, service marks nor logos of NVIDIA Corporation may be used 
 * to endorse or promote products derived from the NVIDIA Software without 
 * specific prior written permission from NVIDIA.  Except as expressly stated 
 * in this notice, no other rights or licenses express or implied, are granted 
 * by NVIDIA herein, including but not limited to any patent rights that may be 
 * infringed by your derivative works or by other works in which the NVIDIA 
 * Software may be incorporated. No hardware is licensed hereunder. 
 * 
 *  
 * 
 * THE NVIDIA SOFTWARE IS BEING PROVIDED ON AN "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING 
 * WITHOUT LIMITATION, WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR ITS USE AND OPERATION 
 * EITHER ALONE OR IN COMBINATION WITH OTHER PRODUCTS.
 * 
 *  
 * 
 * IN NO EVENT SHALL NVIDIA BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL, 
 * EXEMPLARY, CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, LOST 
 * PROFITS; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) OR ARISING IN ANY WAY OUT OF THE USE, 
 * REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE NVIDIA SOFTWARE, 
 * HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING 
 * NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF NVIDIA HAS BEEN ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */ 


#ifndef _cg_h
#define _cg_h


#define CG_VERSION_1_2                1
#define CG_VERSION_NUM                1200


//
// This #define foreces the old API for now.  This will be removed soon, but
// the user will still have the ability to enable it.
//
// #define CG_DEPRECATED_1_1_API 1

// Set up for either Win32 import/export/lib.
#ifndef CGDLL_API
#ifdef WIN32
    #ifdef CGDLL_EXPORTS
    #define CGDLL_API /*__declspec(dllexport) */
    #elif defined (CG_LIB)
    #define CGDLL_API
    #else
    #define CGDLL_API __declspec(dllimport)
    #endif
#else
    #define CGDLL_API
#endif
#endif

/*************************************************************************/
/*** CG Run-Time Library API                                          ***/
/*************************************************************************/

/*************************************************************************/
/*** Data types and enumerants                                         ***/
/*************************************************************************/

typedef int CGbool;

#define CG_FALSE ((CGbool)0)
#define CG_TRUE ((CGbool)1)

//  typedef struct _CGcontext *CGcontext;
//  typedef struct _CGprogram *CGprogram;
//  typedef struct _CGparameter *CGparameter;

// hack: until typedef resolution is fixed, change the typedef from *CGContext
// to CGContext, and change all references to CGContext in functions to CGContext*
typedef struct _CGcontext  CGcontext;
typedef struct _CGprogram  CGprogram;
typedef struct _CGparameter  CGparameter;

typedef enum
 {
  CG_UNKNOWN_TYPE,
  CG_STRUCT,
  CG_ARRAY,

  CG_TYPE_START_ENUM = 1024,
//# define CG_DATATYPE_MACRO(name, compiler_name, enum_name, ncols, nrows) enum_name ,

#include <CG/cg_datatypes.h>

 } CGtype;

typedef enum
 {
//# define CG_BINDLOCATION_MACRO(name,enum_name,compiler_name,enum_int,addressable,param_type) enum_name = enum_int,

#include <CG/cg_bindlocations.h>

  CG_UNDEFINED,

 } CGresource;

typedef enum
 {
  CG_PROFILE_START = 6144,
  CG_PROFILE_UNKNOWN,

//# define CG_PROFILE_MACRO(name, compiler_id, compiler_id_caps, compiler_opt,int_id,vertex_profile) CG_PROFILE_##compiler_id_caps = int_id,
  
#include <CG/cg_profiles.h>

  CG_PROFILE_MAX,
 } CGprofile;

typedef enum
 {
//# define CG_ERROR_MACRO(code, enum_name, new_enum_name, message) new_enum_name = code,
# include <CG/cg_errors.h>
 } CGerror;

typedef enum
 {
//# define CG_ENUM_MACRO(enum_name, enum_val) enum_name = enum_val,
# include <Cg/cg_enums.h>
 } CGenum;

#include <stdarg.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void (*CGerrorCallbackFunc)(void);

/*************************************************************************/
/*** Functions                                                         ***/
/*************************************************************************/

#ifndef CG_EXPLICIT

/*** Context functions ***/

CGDLL_API /*CGcontext*/CGcontext* cgCreateContext(void); 
CGDLL_API void cgDestroyContext(/*CGcontext*/CGcontext* ctx); 
CGDLL_API CGbool cgIsContext(/*CGcontext*/CGcontext* ctx);
CGDLL_API const char *cgGetLastListing(/*CGcontext*/CGcontext* ctx);
CGDLL_API void cgSetAutoCompile(/*CGcontext*/CGcontext* ctx, CGenum flag);

/*** Program functions ***/
CGDLL_API /*CGprogram*/CGprogram* cgCreateProgram(/*CGcontext*/CGcontext* ctx, 
                                    CGenum program_type,
                                    const char *program,
                                    CGprofile profile,
                                    const char *entry,
                                    const char **args);
CGDLL_API /*CGprogram*/CGprogram* cgCreateProgramFromFile(/*CGcontext*/CGcontext* ctx, 
                                            CGenum program_type,
                                            const char *program_file,
                                            CGprofile profile,
                                            const char *entry,
                                            const char **args);
CGDLL_API /*CGprogram*/CGprogram* cgCopyProgram(/*CGprogram*/CGprogram* program); 
CGDLL_API void cgDestroyProgram(/*CGprogram*/CGprogram* program); 

CGDLL_API /*CGprogram*/CGprogram* cgGetFirstProgram(/*CGcontext*/CGcontext* ctx);
CGDLL_API /*CGprogram*/CGprogram* cgGetNextProgram(/*CGprogram*/CGprogram* current);
CGDLL_API /*CGcontext*/CGcontext* cgGetProgramContext(/*CGprogram*/CGprogram* prog);
CGDLL_API CGbool cgIsProgram(/*CGprogram*/CGprogram* program); 

CGDLL_API void cgCompileProgram(/*CGprogram*/CGprogram* program); 
CGDLL_API CGbool cgIsProgramCompiled(/*CGprogram*/CGprogram* program); 
CGDLL_API const char *cgGetProgramString(/*CGprogram*/CGprogram* prog, CGenum pname); 
CGDLL_API CGprofile cgGetProgramProfile(/*CGprogram*/CGprogram* prog); 

/*** Parameter functions ***/

CGDLL_API /*CGparameter*/CGparameter* cgCreateParameter(/*CGcontext*/CGcontext* ctx, CGtype type);
CGDLL_API /*CGparameter*/CGparameter* cgCreateParameterArray(/*CGcontext*/CGcontext* ctx,
                                             CGtype type, 
                                             int length);
CGDLL_API /*CGparameter*/CGparameter* cgCreateParameterMultiDimArray(/*CGcontext*/CGcontext* ctx,
                                                     CGtype type,
                                                     int dim, 
                                                     const int *lengths);
CGDLL_API void cgDestroyParameter(/*CGparameter*/CGparameter* param);
CGDLL_API void cgConnectParameter(/*CGparameter*/CGparameter* from, /*CGparameter*/CGparameter* to);
CGDLL_API void cgDisconnectParameter(/*CGparameter*/CGparameter* param);
CGDLL_API /*CGparameter*/CGparameter* cgGetConnectedParameter(/*CGparameter*/CGparameter* param);

CGDLL_API int cgGetNumConnectedToParameters(/*CGparameter*/CGparameter* param);
CGDLL_API /*CGparameter*/CGparameter* cgGetConnectedToParameter(/*CGparameter*/CGparameter* param, int index);

CGDLL_API /*CGparameter*/CGparameter* cgGetNamedParameter(/*CGprogram*/CGprogram* prog, const char *name);
CGDLL_API /*CGparameter*/CGparameter* cgGetNamedProgramParameter(/*CGprogram*/CGprogram* prog, 
                                                 CGenum name_space, 
                                                 const char *name);

CGDLL_API /*CGparameter*/CGparameter* cgGetFirstParameter(/*CGprogram*/CGprogram* prog, CGenum name_space);
CGDLL_API /*CGparameter*/CGparameter* cgGetNextParameter(/*CGparameter*/CGparameter* current);
CGDLL_API /*CGparameter*/CGparameter* cgGetFirstLeafParameter(/*CGprogram*/CGprogram* prog, CGenum name_space);
CGDLL_API /*CGparameter*/CGparameter* cgGetNextLeafParameter(/*CGparameter*/CGparameter* current);

CGDLL_API /*CGparameter*/CGparameter* cgGetFirstStructParameter(/*CGparameter*/CGparameter* param);
CGDLL_API /*CGparameter*/CGparameter* cgGetNamedStructParameter(/*CGparameter*/CGparameter* param, 
                                                const char *name);

CGDLL_API /*CGparameter*/CGparameter* cgGetFirstDependentParameter(/*CGparameter*/CGparameter* param);

CGDLL_API /*CGparameter*/CGparameter* cgGetArrayParameter(/*CGparameter*/CGparameter* aparam, int index);
CGDLL_API int cgGetArrayDimension(/*CGparameter*/CGparameter* param);
CGDLL_API CGtype cgGetArrayType(/*CGparameter*/CGparameter* param);
CGDLL_API int cgGetArraySize(/*CGparameter*/CGparameter* param, int dimension);
CGDLL_API void cgSetArraySize(/*CGparameter*/CGparameter* param, int size);
CGDLL_API void cgSetMultiDimArraySize(/*CGparameter*/CGparameter* param, const int *sizes);

CGDLL_API /*CGprogram*/CGprogram* cgGetParameterProgram(/*CGparameter*/CGparameter* prog);
CGDLL_API /*CGcontext*/CGcontext* cgGetParameterContext(/*CGparameter*/CGparameter* param);
CGDLL_API CGbool cgIsParameter(/*CGparameter*/CGparameter* param);
CGDLL_API const char *cgGetParameterName(/*CGparameter*/CGparameter* param);
CGDLL_API CGtype cgGetParameterType(/*CGparameter*/CGparameter* param);
CGDLL_API CGtype cgGetParameterNamedType(/*CGparameter*/CGparameter* param);
CGDLL_API const char *cgGetParameterSemantic(/*CGparameter*/CGparameter* param);
CGDLL_API CGresource cgGetParameterResource(/*CGparameter*/CGparameter* param);
CGDLL_API CGresource cgGetParameterBaseResource(/*CGparameter*/CGparameter* param);
CGDLL_API unsigned long cgGetParameterResourceIndex(/*CGparameter*/CGparameter* param);
CGDLL_API CGenum cgGetParameterVariability(/*CGparameter*/CGparameter* param);
CGDLL_API CGenum cgGetParameterDirection(/*CGparameter*/CGparameter* param);
CGDLL_API CGbool cgIsParameterReferenced(/*CGparameter*/CGparameter* param);
CGDLL_API const double *cgGetParameterValues(/*CGparameter*/CGparameter* param, 
                                             CGenum value_type,
                                             int *nvalues);
CGDLL_API int cgGetParameterOrdinalNumber(/*CGparameter*/CGparameter* param);
CGDLL_API CGbool cgIsParameterGlobal(/*CGparameter*/CGparameter* param);
CGDLL_API int cgGetParameterIndex(/*CGparameter*/CGparameter* param);

CGDLL_API void cgSetParameterVariability(/*CGparameter*/CGparameter* param, CGenum vary);
CGDLL_API void cgSetParameterSemantic(/*CGparameter*/CGparameter* param, const char *semantic);


CGDLL_API void cgSetParameter1f(/*CGparameter*/CGparameter* param, float x);
CGDLL_API void cgSetParameter2f(/*CGparameter*/CGparameter* param, float x, float y);
CGDLL_API void cgSetParameter3f(/*CGparameter*/CGparameter* param, float x, float y, float z);
CGDLL_API void cgSetParameter4f(/*CGparameter*/CGparameter* param, 
                                float x, 
                                float y, 
                                float z,
                                float w);
CGDLL_API void cgSetParameter1d(/*CGparameter*/CGparameter* param, double x);
CGDLL_API void cgSetParameter2d(/*CGparameter*/CGparameter* param, double x, double y);
CGDLL_API void cgSetParameter3d(/*CGparameter*/CGparameter* param, 
                                double x, 
                                double y, 
                                double z);
CGDLL_API void cgSetParameter4d(/*CGparameter*/CGparameter* param, 
                                double x, 
                                double y, 
                                double z,
                                double w);


CGDLL_API void cgSetParameter1fv(/*CGparameter*/CGparameter* param, const float *v);
CGDLL_API void cgSetParameter2fv(/*CGparameter*/CGparameter* param, const float *v);
CGDLL_API void cgSetParameter3fv(/*CGparameter*/CGparameter* param, const float *v);
CGDLL_API void cgSetParameter4fv(/*CGparameter*/CGparameter* param, const float *v);
CGDLL_API void cgSetParameter1dv(/*CGparameter*/CGparameter* param, const double *v);
CGDLL_API void cgSetParameter2dv(/*CGparameter*/CGparameter* param, const double *v);
CGDLL_API void cgSetParameter3dv(/*CGparameter*/CGparameter* param, const double *v);
CGDLL_API void cgSetParameter4dv(/*CGparameter*/CGparameter* param, const double *v);

CGDLL_API void cgSetMatrixParameterdr(/*CGparameter*/CGparameter* param, const double *matrix);
CGDLL_API void cgSetMatrixParameterfr(/*CGparameter*/CGparameter* param, const float *matrix);
CGDLL_API void cgSetMatrixParameterdc(/*CGparameter*/CGparameter* param, const double *matrix);
CGDLL_API void cgSetMatrixParameterfc(/*CGparameter*/CGparameter* param, const float *matrix);


/*** Type Functions ***/

CGDLL_API const char *cgGetTypeString(CGtype type);
CGDLL_API CGtype cgGetType(const char *type_string);

CGDLL_API CGtype cgGetNamedUserType(/*CGprogram*/CGprogram* program, const char *name);

CGDLL_API int cgGetNumUserTypes(/*CGprogram*/CGprogram* program);
CGDLL_API CGtype cgGetUserType(/*CGprogram*/CGprogram* program, int index);

CGDLL_API int cgGetNumParentTypes(CGtype type);
CGDLL_API CGtype cgGetParentType(CGtype type, int index);

CGDLL_API CGbool cgIsParentType(CGtype parent, CGtype child);
CGDLL_API CGbool cgIsInterfaceType(CGtype type);

/*** Resource Functions ***/

CGDLL_API const char *cgGetResourceString(CGresource resource);
CGDLL_API CGresource cgGetResource(const char *resource_string);

/*** Enum Functions ***/

CGDLL_API const char *cgGetEnumString(CGenum en);
CGDLL_API CGenum cgGetEnum(const char *enum_string);

/*** Profile Functions ***/

CGDLL_API const char *cgGetProfileString(CGprofile profile);
CGDLL_API CGprofile cgGetProfile(const char *profile_string);

/*** Error Functions ***/

CGDLL_API CGerror cgGetError(void);
CGDLL_API const char *cgGetErrorString(CGerror error);
CGDLL_API const char *cgGetLastErrorString(CGerror *error);
CGDLL_API void cgSetErrorCallback(CGerrorCallbackFunc func);
CGDLL_API CGerrorCallbackFunc cgGetErrorCallback(void);

/*** Misc Functions ***/

CGDLL_API const char *cgGetString(CGenum sname);


/*** Support for deprecated Cg 1.1 API ***/

CGDLL_API /*CGparameter*/CGparameter* cgGetNextParameter_depr1_1(/*CGparameter*/CGparameter* current);
CGDLL_API /*CGparameter*/CGparameter* cgGetNextLeafParameter_depr1_1(/*CGparameter*/CGparameter* current);

#ifdef CG_DEPRECATED_1_1_API

#define cgGetNextParameter cgGetNextParameter_depr1_1
#define cgGetNextLeafParameter cgGetNextLeafParameter_depr1_1

#endif

#endif

#ifdef __cplusplus
}
#endif


#endif
